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

package edu.dfci.cccb.mev.common.domain.guice.configuration;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.reloading.ReloadingStrategy;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.ProvisionListener;

import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;

/**
 * Configures injectable configuration
 * 
 * @author levk
 * @since CRYSTAL
 */
public class ConfigurationModule implements Module {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    binder.install (new SingletonModule () {
      @Provides
      public ReloadingStrategy strategy () {
        return new ServiceLoadedCompoundReloadingStrategy ();
      }

      @Override
      public void configure (Binder binder) {
        binder.bindListener (new AbstractMatcher<Binding<?>> () {

          @Override
          public boolean matches (Binding<?> t) {
            return Configuration.class.isAssignableFrom (t.getKey ().getTypeLiteral ().getRawType ());
          }
        }, new ProvisionListener () {
          private @Inject Provider<ReloadingStrategy> strategy;

          @Override
          public <T> void onProvision (ProvisionInvocation<T> provision) {
            Object provisioned = provision.provision ();
            if (provisioned instanceof FileConfiguration)
              ((FileConfiguration) provision.provision ()).setReloadingStrategy (strategy.get ());
          }
        });
      }
    });
  }
}
