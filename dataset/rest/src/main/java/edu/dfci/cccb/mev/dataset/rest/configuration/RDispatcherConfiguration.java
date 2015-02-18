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
package edu.dfci.cccb.mev.dataset.rest.configuration;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.domain.r.RDispatcher;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Rserve;

/**
 * @author levk
 * 
 */
@Configuration
public class RDispatcherConfiguration {

  @Bean
  @Scope (SCOPE_SINGLETON)
  @Rserve
  public ObjectMapper mapper () {
    return new ObjectMapper ();
  }

  @Bean
  @Scope (SCOPE_SINGLETON)
  public RDispatcher r () {
    return new RDispatcher ();
  }

  @Bean
  @Rserve
  @Scope (SCOPE_SINGLETON)
  public Iterator<InetSocketAddress> hosts () throws ConfigurationException {
    final PropertiesConfiguration config = new PropertiesConfiguration ();
    InputStream configurationStream = getClass ().getResourceAsStream ("/rserve.properties");
    if (configurationStream != null)
      config.load (configurationStream);
    else
      config.setProperty ("rserve.host", "localhost:6311");

    final String[] hosts = config.getStringArray ("rserve.host");
    final InetSocketAddress[] socks = new InetSocketAddress[hosts.length];
    for (int i = socks.length; --i >= 0;) {
      String[] split = hosts[i].split (":");
      socks[i] = new InetSocketAddress (split[0], split.length > 1 ? Integer.parseInt (split[1]) : 6311);
    }

    return new Iterator<InetSocketAddress> () {
      private int index = 0;

      @Override
      public boolean hasNext () {
        return true;
      }

      @Override
      public InetSocketAddress next () {
        if (index >= socks.length)
          index = 0;
        return socks[index++];
      }

      @Override
      public void remove () {}
    };
  }

  @Bean
  @Rserve
  @Scope (SCOPE_PROTOTYPE)
  public InetSocketAddress host (Iterator<InetSocketAddress> hosts) {
    return hosts.next ();
  }
}
