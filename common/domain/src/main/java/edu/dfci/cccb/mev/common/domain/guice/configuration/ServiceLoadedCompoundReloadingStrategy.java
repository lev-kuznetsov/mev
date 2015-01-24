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

import static java.util.ServiceLoader.load;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceConfigurationError;

import javax.inject.Singleton;

import lombok.extern.log4j.Log4j;

import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.reloading.ReloadingStrategy;

/**
 * @author levk
 * @since CRYSTAL
 */
@Log4j
@Singleton
public class ServiceLoadedCompoundReloadingStrategy implements ReloadingStrategy {

  private final Collection<ReloadingStrategy> strategies;

  {
    strategies = new ArrayList<> ();
    for (Iterator<ReloadingStrategy> iterator = load (ReloadingStrategy.class).iterator (); iterator.hasNext ();)
      try {
        strategies.add (iterator.next ());
      } catch (ServiceConfigurationError e) {
        log.warn ("Unable to configure reloading strategy", e);
      }
  }

  @Override
  public void setConfiguration (FileConfiguration configuration) {
    for (ReloadingStrategy strategy : strategies)
      strategy.setConfiguration (configuration);
  }

  @Override
  public boolean reloadingRequired () {
    for (ReloadingStrategy strategy : strategies)
      if (strategy.reloadingRequired ())
        return true;
    return false;
  }

  @Override
  public void reloadingPerformed () {
    for (ReloadingStrategy strategy : strategies)
      strategy.reloadingPerformed ();
  }

  @Override
  public void init () {
    for (ReloadingStrategy strategy : strategies)
      strategy.init ();
  }
}
