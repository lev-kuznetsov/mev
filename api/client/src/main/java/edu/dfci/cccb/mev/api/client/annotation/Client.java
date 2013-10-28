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
package edu.dfci.cccb.mev.api.client.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import edu.dfci.cccb.mev.api.client.annotation.view.Language;

/**
 * @author levk
 * 
 */
@Retention (RUNTIME)
@Target (PACKAGE)
public @interface Client {

  @Retention (RUNTIME)
  @Target (ANNOTATION_TYPE)
  public @interface Resources {
    String[] export ();

    String[] classpath ();
  }

  Resources[] resources () default {};

  @Retention (RUNTIME)
  @Target (ANNOTATION_TYPE)
  public @interface Javascript {
    String injector ();
  }

  Javascript[] javascript () default {};

  @Retention (RUNTIME)
  @Target (ANNOTATION_TYPE)
  public @interface Views {
    Language language ();

    String[] classpath ();
  }

  Views[] views () default {};
}
