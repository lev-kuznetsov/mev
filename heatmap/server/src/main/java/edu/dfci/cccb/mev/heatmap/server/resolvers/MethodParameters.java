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
package edu.dfci.cccb.mev.heatmap.server.resolvers;

import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.join;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.MethodParameter;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.convert.Converter;

/**
 * @author levk
 * 
 */
public final class MethodParameters {

  public static String brief (MethodParameter parameter) {

    return (join (convert (parameter.getParameterAnnotations (), new Converter<Annotation, String> () {

      @Override
      public String convert (Annotation from) {
        return "@"
               + from.annotationType ().getSimpleName () + " ("
               + join (Lambda.convert (fields (from).entrySet (), new Converter<Entry<String, String>, String> () {

                 @Override
                 public String convert (Entry<String, String> from) {
                   return from.getKey () + "=" + from.getValue ();
                 }
               }), ", ") + ")";
      }

      private Map<String, String> fields (Annotation from) {
        Map<String, String> result = new HashMap<> ();
        for (Method field : from.annotationType ().getMethods ())
          if (!field.getDeclaringClass ().equals (Annotation.class)
              && !field.getDeclaringClass ().equals (Object.class))
            try {
              result.put (field.getName (), toString (field.invoke (from)));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
              result.put (field.getName (), e.getMessage ());
            }
        return result;
      }

      private String toString (Object o) {
        return o == null ? "null" : o.toString ().trim ();
      }
    })) + " " + parameter.getParameterType ().getSimpleName ()).trim ();
  }

  private MethodParameters () {}
}
