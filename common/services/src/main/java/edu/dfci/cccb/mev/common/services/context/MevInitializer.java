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

package edu.dfci.cccb.mev.common.services.context;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.bindProperties;
import static edu.dfci.cccb.mev.common.domain.guice.MevDomainModule.load;
import static edu.dfci.cccb.mev.common.domain.guice.Modules.discover;

import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.servlet.annotation.WebListener;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Initialization hook
 * <p/>
 * This class gets picked up by a servlet 3 container due to the
 * {@link WebListener} annotation from here I load Guice modules discovered via
 * the {@link ServiceLoader} API
 * 
 * @author levk
 * @since ASHA
 */
@WebListener
public class MevInitializer extends GuiceServletContextListener {

  private static final String[] CONFIGURATION = new String[] {};

  /* (non-Javadoc)
   * @see com.google.inject.servlet.GuiceServletContextListener#getInjector() */
  @Override
  protected Injector getInjector () {
    final Injector context = createContext (load (CONFIGURATION));

    return context.createChildInjector (new Iterable<Module> () {
      private final Iterable<Module> modules = discover ();

      @Override
      public Iterator<Module> iterator () {
        return new Iterator<Module> () {
          private final Iterator<Module> iterator = modules.iterator ();

          @Override
          public boolean hasNext () {
            return iterator.hasNext ();
          }

          @Override
          public Module next () {
            Module module = iterator.next ();
            context.injectMembers (module);
            return module;
          }

          @Override
          public void remove () {
            iterator.remove ();
          }
        };
      }
    });
  }

  /**
   * @param properties
   * @return property injector
   */
  private Injector createContext (final Properties... properties) {
    return createInjector (new AbstractModule () {

      @Override
      protected void configure () {
        for (Properties each : properties)
          bindProperties (binder (), each);
      }
    });
  }
}
