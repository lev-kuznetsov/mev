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

package edu.dfci.cccb.mev.common.domain.guice.jackson.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import javax.ws.rs.core.MediaType;

/**
 * @author levk
 * @since CRYSTAL
 */
@Retention (RUNTIME)
@Target ({ FIELD, METHOD, PARAMETER })
@Qualifier
@Documented
public @interface Handling {

  /**
   * Mime type
   */
  String value ();

  public static class Factory {

    public static Handling APPLICATION_JSON = handling (MediaType.APPLICATION_JSON);
    public static Handling TEXT_TSV = handling ("text/tab-separated-values");

    public static Handling handling (final String mime) {
      if (mime == null)
        throw new NullPointerException ("Mime type cannot be null");
      return new Handling () {

        @Override
        public Class<? extends Annotation> annotationType () {
          return Handling.class;
        }

        @Override
        public String value () {
          return mime;
        }

        @Override
        public boolean equals (Object obj) {
          return obj instanceof Handling ? ((Handling) obj).value ().equals (mime) : false;
        }

        @Override
        public int hashCode () {
          // This is specified in java.lang.Annotation.
          return (127 * "value".hashCode ()) ^ value ().hashCode ();
        }

        @Override
        public String toString () {
          return "@" + Handling.class.getName () + "(value=" + mime + ")";
        }
      };
    }
  }
}
