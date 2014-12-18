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

package edu.dfci.cccb.mev.common.domain.guice.rserve;

import static com.google.inject.internal.Annotations.findBindingAnnotation;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.net.InetSocketAddress.createUnresolved;
import static java.util.UUID.randomUUID;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RSession;
import org.rosuda.REngine.Rserve.RserveException;

import com.google.inject.Binder;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Errors;
import com.google.inject.multibindings.Multibinder;

import edu.dfci.cccb.mev.common.domain.guice.rserve.annotation.Rserve;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Callback;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Error;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Result;
import edu.dfci.cccb.mev.common.domain.jobs.r.Deserializer;
import edu.dfci.cccb.mev.common.domain.jobs.r.Serializer;
import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.R;
import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.toR;
import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.toR.Primitives;

/**
 * @author levk
 * @since CRYSTAL
 */
@Log4j
public class RserveModule implements Module {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (final Binder binder) {
    configure (new RserveHostConfigurer () {
      private final Multibinder<InetSocketAddress> hosts = newSetBinder (binder, InetSocketAddress.class, Rserve.class);

      @Override
      public FeatureSet add (InetSocketAddress address) {
        hosts.addBinding ().toInstance (address);

        return new FeatureSet () {};
      }
    });

    // Primitive round robin load balancer
    binder.bind (RConnection.class).toProvider (new Provider<RConnection> () {
      private Iterator<InetSocketAddress> hosts;
      private Map<InetSocketAddress, Collection<Throwable>> errors;

      @Inject
      private void hosts (final @Rserve Set<InetSocketAddress> all) {
        final Set<InetSocketAddress> hosts = new HashSet<> (all);
        this.hosts = new Iterator<InetSocketAddress> () {
          private Iterator<InetSocketAddress> iterator = hosts.iterator ();

          @Override
          public boolean hasNext () {
            return true;
          }

          @Override
          @Synchronized
          public InetSocketAddress next () {
            if (!iterator.hasNext ())
              iterator = hosts.iterator ();
            return iterator.next ();
          }

          @Override
          public void remove () {
            iterator.remove ();
          }
        };

        errors = new HashMap<InetSocketAddress, Collection<Throwable>> () {
          private static final long serialVersionUID = 1L;

          @Override
          @Synchronized
          public Collection<Throwable> get (Object key) {
            Collection<Throwable> result = super.get (key);
            if (result == null && key instanceof InetSocketAddress)
              put ((InetSocketAddress) key, result = new ArrayList<> ());
            return result;
          }
        };
      }

      @Override
      public RConnection get () {
        for (;;) {
          InetSocketAddress next = hosts.next ();
          try {
            return new RConnection (next.getHostString (), next.getPort ());
          } catch (RserveException e) {
            log.warn ("Error attempting to establish connection to Rserve at " + next, e);
            if (errors.get (next).add (e)) // Removes on first fail
              hosts.remove ();
          }
        }
      }
    });

    // Injector
    binder.bind (edu.dfci.cccb.mev.common.domain.jobs.r.R.class)
          .toProvider (new Provider<edu.dfci.cccb.mev.common.domain.jobs.r.R> () {
            @Override
            public edu.dfci.cccb.mev.common.domain.jobs.r.R get () {
              return new edu.dfci.cccb.mev.common.domain.jobs.r.R () {

                private @Inject Injector injector;
                private @Inject Provider<RConnection> connection;

                @Override
                public void close () throws Exception {}

                @SuppressWarnings ("unchecked")
                @SneakyThrows (IllegalAccessException.class)
                private String toR (Object function, Field parameter) {
                  parameter.setAccessible (true);
                  Object value = parameter.get (function);
                  toR annotation = parameter.getAnnotation (toR.class);
                  if (annotation == null && value != null)
                    annotation = value.getClass ().getAnnotation (toR.class);
                  Class<? extends Serializer<?>> type = annotation == null ? Primitives.class : annotation.value ();
                  return ((Serializer<Object>) injector.getInstance (type)).serialize (value);
                }

                @SneakyThrows (IllegalAccessException.class)
                private void toJava (REXP r, Object function, Field target) {
                  target.setAccessible (true);
                  edu.dfci.cccb.mev.common.domain.jobs.r.annotation.toJava annotation =
                                                                                        target.getAnnotation (edu.dfci.cccb.mev.common.domain.jobs.r.annotation.toJava.class);
                  if (annotation == null)
                    annotation = target.getType ()
                                       .getAnnotation (edu.dfci.cccb.mev.common.domain.jobs.r.annotation.toJava.class);
                  Class<? extends Deserializer> type =
                                                       annotation == null
                                                                         ? edu.dfci.cccb.mev.common.domain.jobs.r.annotation.toJava.Primitives.class
                                                                         : annotation.value ();
                  target.set (function, ((Deserializer) injector.getInstance (type)).deserialize (r));
                }

                @SneakyThrows ({ InvocationTargetException.class, IllegalAccessException.class })
                private void invokeInjected (Object function, Method method) {
                  Object[] arguments = new Object[method.getParameterTypes ().length];
                  Errors errors = new Errors ();
                  for (int i = arguments.length; --i >= 0;) {
                    Annotation qualifier = findBindingAnnotation (errors, method, method.getParameterAnnotations ()[i]);
                    TypeLiteral<?> type = TypeLiteral.get (function.getClass ()).getParameterTypes (method).get (i);
                    Key<?> key = qualifier == null ? Key.get (type) : Key.get (type, qualifier);
                    arguments[i] = injector.getInstance (key);
                  }
                  if (errors.hasErrors ())
                    throw new ConfigurationException (errors.getMessages ());
                  method.setAccessible (true);
                  method.invoke (function, arguments);
                }

                @Override
                public void inject (Object function) throws Exception {

                  // Call in the form of:
                  // v <- try (binder (callback = function (binder) {
                  // // define ('param', function () value, b = binder);
                  // // # ...
                  // // inject (function, binder);
                  // }), silent = TRUE);

                  UUID uuid = randomUUID ();
                  String v = "v" + uuid.getMostSignificantBits () + "." + uuid.getLeastSignificantBits ();
                  final StringBuilder command = new StringBuilder ();
                  command.append (v + " <- try (binder (callback = function (binder) { ");
                  for (Class<?> clazz = function.getClass (); clazz != null; clazz = clazz.getSuperclass ())
                    for (Field field : clazz.getFields ()) {
                      Parameter annotation = field.getAnnotation (Parameter.class);
                      if (annotation != null) {
                        String key = annotation.value ().equals ("") ? field.getName () : annotation.value ();
                        command.append ("define ('"
                                        + key + "', function () " + toR (function, field) + ", b = binder); ");
                      }
                    }
                  command.append ("inject (" + function.getClass ().getAnnotation (R.class).value ()
                                  + ", binder); }), silent = TRUE);");

                  RSession session = connection.get ().voidEvalDetach (command.toString ());
                  RConnection connection = session.attach (); // Blocks until
                                                              // previous
                                                              // command
                                                              // completes
                  try {
                    REXP result = connection.eval (v);
                    if (!result.inherits ("try-error")) {
                      for (Class<?> clazz = function.getClass (); clazz != null; clazz = clazz.getSuperclass ()) {
                        for (Field field : clazz.getFields ())
                          if (field.isAnnotationPresent (Result.class))
                            toJava (result, function, field);
                        for (Method method : clazz.getMethods ())
                          if (method.isAnnotationPresent (Callback.class) && !method.isAnnotationPresent (Error.class))
                            invokeInjected (function, method);
                      }
                    } else {
                      for (Class<?> clazz = function.getClass (); clazz != null; clazz = clazz.getSuperclass ()) {
                        for (Field field : clazz.getFields ())
                          if (field.isAnnotationPresent (Error.class))
                            toJava (result, function, field);
                        for (Method method : clazz.getMethods ())
                          if (method.isAnnotationPresent (Callback.class) && !method.isAnnotationPresent (Result.class))
                            invokeInjected (function, method);
                      }
                    }
                  } finally {
                    connection.eval ("rm (" + v + ");");
                    connection.close ();
                  }
                }
              };
            }
          });
  }

  /**
   * Configures Rserve hosts
   */
  public void configure (final RserveHostConfigurer configurer) {
    configurer.add (createUnresolved ("localhost", 6311));
  }
}
