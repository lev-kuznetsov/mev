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

package edu.dfci.cccb.mev.common.domain.jobs.r.annotation;

import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.join;
import static java.lang.Boolean.TRUE;
import static java.lang.String.valueOf;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import ch.lambdaj.function.convert.Converter;
import edu.dfci.cccb.mev.common.domain.jobs.r.Serializer;

/**
 * @author levk
 * @since CRYSTAL
 */
@Target ({ FIELD, TYPE })
@Retention (RUNTIME)
@Documented
@Inherited
public @interface toR {

  /**
   * Type capable of serializing
   */
  Class<? extends Serializer<?>> value ();

  /**
   * Default {@link Serializer} implementation responsible for serializing
   * primitives
   */
  public static class Primitives implements Serializer<Object> {
    @SuppressWarnings ("unchecked")
    @Override
    public String serialize (Object o) {
      if (o == null)
        return "NULL";
      else if (o instanceof Boolean)
        return TRUE.equals (o) ? "TRUE" : "FALSE";
      else if (o instanceof String)
        return "'" + o + "'";
      else if (o instanceof Number)
        return valueOf (((Number) o).doubleValue ());
      else if (o instanceof Collection)
        return "c (" + join (convert (o, new Converter<Object, String> () {
          @Override
          public String convert (Object from) {
            return serialize (from);
          }
        }), ", ") + ")";
      else if (o instanceof Map)
        return "list ("
               + join (convert (((Map<Object, Object>) o).entrySet (),
                                new Converter<Entry<Object, Object>, String> () {

                                  @Override
                                  public String convert (Entry<Object, Object> from) {
                                    return serialize (from.getKey ()) + " = " + serialize (from.getValue ());
                                  }
                                }),
                       ", ") + ")";
      else
        throw new IllegalArgumentException ("Unable to coerce "
                                            + o + " of type " + o.getClass ().getSimpleName ()
                                            + " to a possible representaiton, please implement an Adapter and use"
                                            + " the @toR plugin annotation");
    }
  };
}
