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
package edu.dfci.cccb.mev.dataset.domain.support;

import static java.util.Arrays.asList;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.PreDestroy;

import lombok.extern.log4j.Log4j;

/**
 * @author levk
 * 
 */
@Log4j
public final class LifecycleUtilities {

  public static <E extends Exception> void destroy (E exception, Object... objects) throws E {
    for (Object object : objects) {
      if (log.isDebugEnabled ())
        log.debug ("Destroying " + object);
      
      if (object instanceof AutoCloseable){
        try {
          if (log.isDebugEnabled ())
            log.debug ("Invoking inferred destruction method AutoCloseable.close() for " + object);
          ((AutoCloseable) object).close ();
          return;
        } catch (Exception e) {
          exception.addSuppressed (e);
          throw exception;
        }
      }
      
      for (Method method : object.getClass ().getMethods ()){
        if (method.isAnnotationPresent (PreDestroy.class)) {
          if (method.getParameterTypes ().length > 0)
            exception.addSuppressed (new IllegalArgumentException ("@PreDestroy method in "
                                                                   + object.getClass () + " implementing "
                                                                   + asList (object.getClass ().getInterfaces ())
                                                                   + " has arguments"));
          else if (!"close".equals (method.getName ()))
            try {
              if (log.isDebugEnabled ())
                log.debug ("Invoking annotated destruction method " + method.getName () + " for " + object);
              method.invoke (object);
              return;
            } catch (Exception e) {
              exception.addSuppressed (e);
            }          
        }
      }
    
      
      if (object instanceof Object[])
        for (Object element : (Object[]) object)
          try {
            if (log.isDebugEnabled ())
              log.debug ("Inferred array");
            destroy (exception, element);
          } catch (Exception e) {}
      else if (object instanceof Iterable<?>)
        for (Object element : (Iterable<?>) object)
          try {
            if (log.isDebugEnabled ())
              log.debug ("Inferred collection");
            destroy (exception, element);
          } catch (Exception e) {}
      else if (object instanceof Map<?, ?>) {
        try {
          destroy (exception, ((Map<?, ?>) object).keySet ());
        } catch (Exception e) {}
        try {
          destroy (exception, ((Map<?, ?>) object).values ());
        } catch (Exception e) {}
      }      
    }

    if (exception.getSuppressed ().length > 0)
      throw exception;
  }

  private LifecycleUtilities () {}
}
