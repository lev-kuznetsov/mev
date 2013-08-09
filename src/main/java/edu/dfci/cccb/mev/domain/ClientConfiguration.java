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
package edu.dfci.cccb.mev.domain;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

import java.util.Properties;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import edu.dfci.cccb.mev.controllers.ClientConfigurationKeyNotFoundException;

/**
 * @author levk
 * 
 */
@Component
@Scope (value = SCOPE_SESSION, proxyMode = TARGET_CLASS)
@Log4j
public class ClientConfiguration {

  private @Autowired Environment defaultConfiguration;
  private Properties properties = new Properties ();

  public String get (String key) throws ClientConfigurationKeyNotFoundException {
    if (log.isDebugEnabled ())
      log.debug ("Looking up configuration key " + key);
    String result = properties.getProperty (key, defaultConfiguration.getProperty (key));
    if (result == null)
      throw new ClientConfigurationKeyNotFoundException (key);
    else
      return result;
  }
  
  public void put (String key, String value) {
    properties.put (key, value);
  }
}
