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

import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.named;
import static java.util.ServiceLoader.load;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import lombok.extern.log4j.Log4j;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
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
   * @param properties to inject into loaded modules
   * @return injector with discovered modules installed
   */
  public static Injector discover (final Properties... properties) {
    final Injector configurer = createInjector (new AbstractModule () {

      @Override
      protected void configure () {
        for (Properties propertiesContainer : properties)
          for (Entry<Object, Object> property : propertiesContainer.entrySet ())
            bind (String.class).annotatedWith (named ((String) property.getKey ()))
                               .toInstance ((String) property.getValue ());
      }
    });

    return configurer.createChildInjector (new Iterable<Module> () {

      /* (non-Javadoc)
       * @see java.lang.Iterable#iterator() */
      @Override
      public Iterator<Module> iterator () {
        return new Iterator<Module> () {

          private Iterator<Module> modules = load (Module.class).iterator ();

          /* (non-Javadoc)
           * @see java.util.Iterator#hasNext() */
          @Override
          public boolean hasNext () {
            return modules.hasNext ();
          }

          /* (non-Javadoc)
           * @see java.util.Iterator#next() */
          @Override
          public Module next () {
            Module next = modules.next ();
            configurer.injectMembers (next);
            log.info ("Discovered module " + next);
            return next;
          }

          /* (non-Javadoc)
           * @see java.util.Iterator#remove() */
          @Override
          public void remove () {
            throw new UnsupportedOperationException ();
          }
        };
      }
    });
  }

  /**
   * No instantiation, functions only
   */
  private Modules () {}
}
