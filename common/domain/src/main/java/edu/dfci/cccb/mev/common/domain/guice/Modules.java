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

package edu.dfci.cccb.mev.common.domain.guice;

import static java.util.ServiceLoader.load;

import java.util.Iterator;

import lombok.extern.log4j.Log4j;

import com.google.inject.Module;

/**
 * Module utilities
 * 
 * @author levk
 * @since CRYSTAL
 */
@Log4j
public class Modules {

  /**
   * @return an iteration over discovered modules
   */
  public static Iterable<Module> discover () {
    return new Iterable<Module> () {
      private final Iterable<Module> loader = load (Module.class);

      @Override
      public Iterator<Module> iterator () {
        return new Iterator<Module> () {
          private final Iterator<Module> iterator = loader.iterator ();

          @Override
          public boolean hasNext () {
            return iterator.hasNext ();
          }

          @Override
          public Module next () {
            Module module = iterator.next ();
            log.info ("Loaded " + module.getClass ().getName ());
            return module;
          }

          @Override
          public void remove () {
            iterator.remove ();
          }
        };
      }
    };
  }

  /**
   * No instantiation, functions only
   */
  private Modules () {}
}
