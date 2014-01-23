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
package edu.dfci.cccb.mev.web.configuration.social;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.security.crypto.encrypt.Encryptors.noOpText;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;

/**
 * @author levk
 * 
 */
@Configuration
public class SocialConfiguration {

  @Bean
  public ConnectionFactoryLocator connectionFactoryLocator (@Value ("${google.client.id:}") String googleId,
                                                            @Value ("${google.client.secret:}") String googleSecret) {
    ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry ();
    if (!isBlank (googleId) && !isBlank (googleSecret))
      registry.addConnectionFactory (new GoogleConnectionFactory (googleId, googleSecret));
    return registry;
  }

  @Bean
  public UsersConnectionRepository usersConnectionRepository (DataSource dataSource,
                                                              ConnectionFactoryLocator connectionFactoryLocator) {
    JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository (dataSource,
                                                                                  connectionFactoryLocator,
                                                                                  noOpText ());
    // TODO: set connection sign up
    return repository;
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Google google (ConnectionRepository connectionRepository) {
    return connectionRepository.getPrimaryConnection (Google.class).getApi ();
  }
}
