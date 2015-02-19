/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.domain.r;

import static java.lang.Math.abs;
import static java.util.UUID.randomUUID;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RSession;
import org.rosuda.REngine.Rserve.RserveException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback.CallbackType;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Error;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Rserve;

/**
 * @author levk
 * 
 */
@Log4j
public class RDispatcher {

  private @Inject @Rserve Provider<InetSocketAddress> host;
  private @Inject @Rserve ObjectMapper mapper;
  private Executor dispatcher;

  @Inject
  public void configureScheduler (@Rserve int concurrency) {
    dispatcher = Executors.newFixedThreadPool (2);
  }

  public void schedule (final Object job) {
    dispatcher.execute (new Runnable () {

      @Override
      public void run () {
        execute (job);
      }
    });
  }

  @SneakyThrows (JsonProcessingException.class)
  private RSession define (String name, Object value, RSession to, StringBuffer command) throws RserveException {
    UUID unique = randomUUID ();
    String p = "p." + abs (unique.getMostSignificantBits ()) + "." + abs (unique.getLeastSignificantBits ());
    RConnection c = to.attach ();
    c.assign (p, new REXPString (mapper.writeValueAsString (value)));
    log.debug ("Defining key '" + name + "' for value " + value);
    command.append ("define ('").append (name)
           .append ("', function (fromJson) { ")
           .append ("r <- fromJson (").append (p).append ("); rm (\"").append (p).append ("\", envir = .GlobalEnv); r")
           .append (" }, scope = singleton, binder = binder); ");
    return c.detach ();
  }

  @SneakyThrows (IllegalAccessException.class)
  public void execute (Object job) {
    REXP result = null;
    try {
      InetSocketAddress host = this.host.get ();
      RConnection connection = new RConnection (host.getHostString (), host.getPort ());
      RSession session = connection.detach ();

      UUID unique = randomUUID ();
      String v = "v." + abs (unique.getMostSignificantBits ()) + "." + abs (unique.getLeastSignificantBits ());

      StringBuffer command = new StringBuffer ().append (v).append (" <- try (binder (callback = function (binder) { ");

      try {
        for (Class<?> clazz = job.getClass (); clazz != null; clazz = clazz.getSuperclass ()) {
          for (Field field : clazz.getDeclaredFields ()) {
            Parameter annotation = field.getAnnotation (Parameter.class);
            if (annotation != null) {
              field.setAccessible (true);
              session = define ("".equals (annotation.value ()) ? field.getName () : annotation.value (),
                                field.get (job),
                                session,
                                command);
            }
          }

          for (Method method : clazz.getDeclaredMethods ()) {
            Parameter annotation = method.getAnnotation (Parameter.class);
            if (annotation != null) {
              method.setAccessible (true);
              session = define ("".equals (annotation.value ()) ? method.getName () : annotation.value (),
                                method.invoke (job),
                                session,
                                command);
            }
          }
        }

        command.append ("inject (")
               .append (job.getClass ().getAnnotation (R.class).value ())
               .append (", binder); }), silent = TRUE);");

        for (session = session.attach ().voidEvalDetach (command.toString ());;)
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
        if (connection != null)
          connection.close ();
      }

      for (Class<?> clazz = job.getClass (); clazz != null; clazz = clazz.getSuperclass ())
        if (result.inherits ("try-error"))
          for (Field field : clazz.getDeclaredFields ()) {
            Error annotation = field.getAnnotation (Error.class);
            if (annotation != null) {
              field.setAccessible (true);
              field.set (job, result.asString ());
            }
          }
        else if (result.inherits ("result-json"))
          for (Field field : clazz.getDeclaredFields ()) {
            Result annotation = field.getAnnotation (Result.class);
            if (annotation != null) {
              field.setAccessible (true);
              field.set (job,
                         mapper.readValue (result.asString (),
                                           mapper.getTypeFactory ().constructType (field.getGenericType ())));
            }
          }
        else
          throw new UnsupportedOperationException ("Unrecognized result "
                                                   + result.getClass () + ":" + result.toDebugString ());

      for (Class<?> clazz = job.getClass (); clazz != null; clazz = clazz.getSuperclass ())
        for (Method method : clazz.getDeclaredMethods ()) {
          Callback annotation = method.getAnnotation (Callback.class);
          if (annotation != null) {
            if (result.inherits ("try-error") && annotation.value () != CallbackType.SUCCESS) {
              method.setAccessible (true);
              method.invoke (job);
            } else if (result.inherits ("result-json") && annotation.value () != CallbackType.FAIL) {
              method.setAccessible (true);
              method.invoke (job);
            }
          }
        }
    } catch (RserveException | InvocationTargetException | IllegalArgumentException | REXPMismatchException | IOException e) {
      log.error ("Failure processing job " + job, e);
    }
  }
}
