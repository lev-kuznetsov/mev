/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.common.domain.jobs.r;

import static com.google.inject.Key.get;
import static com.google.inject.internal.Annotations.findBindingAnnotation;
import static java.lang.Math.abs;
import static java.util.UUID.randomUUID;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RSession;
import org.rosuda.REngine.Rserve.RserveException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.internal.Errors;

import edu.dfci.cccb.mev.common.domain.guice.rserve.annotation.Rserve;
import edu.dfci.cccb.mev.common.domain.jobs.Dispatcher;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Callback;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Error;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Result;

/**
 * Dispatches R jobs
 * 
 * @author levk
 * @since CRYSTAL
 */
@Log4j
public class R implements Dispatcher {

  private @Inject @Rserve Provider<InetSocketAddress> host;
  private @Inject @Rserve Executor executor;
  private @Inject ObjectMapper mapper;
  private @Inject @Rserve Set<JsonSerializer<?>> serializers;
  private @Inject Injector injector;

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.common.domain.jobs.Dispatcher#dispatch(java.lang.Object) */
  @Override
  public void dispatch (final Object job) {
    executor.execute (new Runnable () {

      @Override
      public void run () {
        R.this.execute (job);
      }
    });
  }

  @SuppressWarnings ("unchecked")
  @SneakyThrows ({ JsonProcessingException.class, IOException.class })
  private StringBuffer define (String key, Object value, Set<String> parameterNames, Object job) {
    if (!parameterNames.add (key))
      throw new IllegalArgumentException ("Duplicate parameter key " + key);
    JsonSerializer<Object> custom = null;
    if (value != null && serializers != null)
      for (Iterator<JsonSerializer<?>> serializers = this.serializers.iterator (); serializers.hasNext ();) {
        JsonSerializer<?> serializer = serializers.next ();
        if (serializer.handledType ().isAssignableFrom (value.getClass ())) {
          custom = (JsonSerializer<Object>) serializer;
          break;
        }
      }
    StringWriter buffer = new StringWriter ();
    if (custom != null)
      custom.serialize (value,
                        mapper.getFactory ().createGenerator (buffer),
                        mapper.getSerializerProvider ());
    String json = (custom == null ? mapper.writeValueAsString (value) : buffer.toString ()).replaceAll ("\"", "\\\\\"");
    return new StringBuffer ("define ('").append (key)
                                         .append ("', function (fromJson) { ")
                                         .append ("fromJson (\"")
                                         .append (json)
                                         .append ("\")")
                                         .append (" }, scope = singleton, binder = binder); ");
  }

  // Fields and methods are made accessible
  @SneakyThrows ({ IllegalAccessException.class, REXPMismatchException.class })
  private void execute (Object job) {
    try {
      // Call in the form of:
      // v <- try (binder (callback = function (binder) {
      // // define ('param', function () value, b = binder);
      // // # ...
      // // inject (function, binder);
      // }), silent = TRUE);

      StringBuffer command = new StringBuffer ();
      UUID unique = randomUUID ();
      String v = "v." + abs (unique.getMostSignificantBits ()) + "." + abs (unique.getLeastSignificantBits ());

      command.append (v).append (" <- try (binder (callback = function (binder) { ");

      Set<String> parameterNames = new HashSet<> ();
      for (Class<?> clazz = job.getClass (); clazz != null; clazz = clazz.getSuperclass ()) {
        for (Field field : clazz.getDeclaredFields ()) {
          Parameter annotation = field.getAnnotation (Parameter.class);
          if (annotation != null) {
            field.setAccessible (true);
            command.append (define ("".equals (annotation.value ()) ? field.getName () : annotation.value (),
                                    field.get (job), parameterNames, job));
          }
        }
        for (Method method : clazz.getDeclaredMethods ()) {
          Parameter annotation = method.getAnnotation (Parameter.class);
          if (annotation != null) {
            method.setAccessible (true);
            Object[] arguments = new Object[method.getParameterTypes ().length];
            Errors errors = new Errors ();
            for (int i = arguments.length; --i >= 0;) {
              Annotation bindingAnnotation = findBindingAnnotation (errors,
                                                                    method,
                                                                    method.getParameterAnnotations ()[i]);
              arguments[i] = injector.getInstance (bindingAnnotation == null
                                                                            ? Key.get (method.getParameterTypes ()[i])
                                                                            : Key.get (method.getParameterTypes ()[i],
                                                                                       bindingAnnotation));
            }
            if (errors.hasErrors ())
              throw new IllegalArgumentException ("Unable to inject parameter method", errors.toException ());
            command.append (define ("".equals (annotation.value ()) ? method.getName () : annotation.value (),
                                    method.invoke (job, arguments), parameterNames, job));
          }
        }
      }

      command.append ("inject (")
             .append (job.getClass ()
                         .getAnnotation (edu.dfci.cccb.mev.common.domain.jobs.r.annotation.R.class)
                         .value ())
             .append (", binder); }), silent = TRUE);");

      InetSocketAddress host = this.host.get ();
      RConnection connection = null;
      REXP result = null;
      try {
        for (RSession session = new RConnection (host.getHostString (),
                                                 host.getPort ()).voidEvalDetach (command.toString ());;)
          try {
            connection = session.attach ();
            break;
          } catch (RserveException e) {
            log.error (e);
            if (!(e.getCause () instanceof SocketTimeoutException))
              throw e;
          }

        result = connection.eval ("inject (function (result) result (" + v + "));");
        connection.voidEval ("rm (" + v + ")");
      } finally {
        if (connection != null) {
          connection.close ();
        }
      }

      for (Class<?> clazz = job.getClass (); clazz != null; clazz = clazz.getSuperclass ()) {
        if (result.inherits ("try-error")) {
          log.error ("Error during R computation: " + result.toDebugString ());
          for (Field field : clazz.getDeclaredFields ())
            if (field.isAnnotationPresent (Error.class)) {
              field.setAccessible (true);
              field.set (job, result.asString ());
            }
          for (Method method : clazz.getDeclaredMethods ())
            if (method.isAnnotationPresent (Callback.class)) {
              Object[] arguments = new Object[method.getParameterTypes ().length];
              Errors errors = new Errors ();
              argument: for (int index = arguments.length; --index >= 0;) {
                for (Annotation annotation : method.getParameterAnnotations ()[index])
                  if (annotation.annotationType ().equals (Error.class)) {
                    arguments[index] = result.asString ();
                    continue argument;
                  }
                Annotation bindingAnnotation = findBindingAnnotation (errors,
                                                                      method,
                                                                      method.getParameterAnnotations ()[index]);
                Key<?> key = bindingAnnotation == null ? get (method.getParameterTypes ()[index])
                                                      : get (method.getParameterTypes ()[index],
                                                             bindingAnnotation);
                arguments[index] = injector.getInstance (key);
              }
              errors.throwProvisionExceptionIfErrorsExist ();
              method.setAccessible (true);
              method.invoke (job, arguments);
            }
        } else if (result.inherits ("result-json"))
          try {
            for (Field field : clazz.getDeclaredFields ())
              if (field.isAnnotationPresent (Result.class)) {
                field.setAccessible (true);
                field.set (job,
                           mapper.readValue (result.asString (),
                                             mapper.getTypeFactory ().constructType (field.getGenericType ())));
              }
            for (Method method : clazz.getDeclaredMethods ())
              if (method.isAnnotationPresent (Callback.class)) {
                Object[] arguments = new Object[method.getParameterTypes ().length];
                Errors errors = new Errors ();
                argument: for (int index = arguments.length; --index >= 0;) {
                  for (Annotation annotation : method.getParameterAnnotations ()[index])
                    if (annotation.annotationType ().equals (Result.class)) {
                      arguments[index] =
                                         mapper.readValue (result.asString (),
                                                           mapper.getTypeFactory ()
                                                                 .constructType (method.getGenericParameterTypes ()[index]));
                      continue argument;
                    }
                  Annotation bindingAnnotation = findBindingAnnotation (errors,
                                                                        method,
                                                                        method.getParameterAnnotations ()[index]);
                  Key<?> key = bindingAnnotation == null
                                                        ? get (method.getParameterTypes ()[index])
                                                        : get (method.getParameterTypes ()[index],
                                                               bindingAnnotation);
                  arguments[index] = injector.getInstance (key);
                }
                errors.throwProvisionExceptionIfErrorsExist ();
                method.setAccessible (true);
                method.invoke (job, arguments);
              }
          } catch (IOException e) {
            log.error ("Serialization exception while receiving result from R subsystem", e);
          }
        else
          log.error (new UnsupportedOperationException ("Unacceptable response class, response must inherit from result-json "
                                                        + "or try-error, actual REXP "
                                                        + result.getClass ().getName () + ":" + result.toDebugString ()));
      }
    } catch (RserveException | InvocationTargetException e) {
      log.error ("Failure executing job " + job, e);
    }
  }
}
