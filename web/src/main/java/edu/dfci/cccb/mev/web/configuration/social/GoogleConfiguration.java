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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInInterceptor;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.dataset.rest.google.SecurityContext;
import edu.dfci.cccb.mev.web.domain.social.SimpleConnectionSignUp;
import edu.dfci.cccb.mev.web.domain.social.SimpleSignInAdapter;
import edu.dfci.cccb.mev.web.domain.social.UserInterceptor;

/**
 * @author levk
 * 
 */
@Configuration
public class GoogleConfiguration extends WebMvcConfigurerAdapter {

  private @Value ("${GOOGLE_API_KEY:902321796189-et9joehlsn9agecgt6ooct5ean864g40.apps.googleusercontent.com}") String key;
  private @Value ("${GOOGLE_API_SECRET:MJdI8KFgLCd3FG-M5hLb5yd5}") String secret;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer () {
    return new PropertySourcesPlaceholderConfigurer ();
  }

  /**
   * When a new provider is added to the app, register its
   * {@link ConnectionFactory} here.
   * 
   * @see GoogleConnectionFactory
   */
  @Bean
  public ConnectionFactoryLocator connectionFactoryLocator () {
    ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry ();
    registry.addConnectionFactory (new GoogleConnectionFactory (key, secret) {
      {
        setScope ("https://www.googleapis.com/auth/userinfo.profile" +
                  ";https://www.googleapis.com/auth/userinfo.email" +
                  ";https://www.googleapis.com/auth/plus.me" +
                  ";https://www.googleapis.com/auth/plus.login" +
                  ";https://www.googleapis.com/auth/drive" +
                  ";https://www.googleapis.com/auth/drive.appdata" +
                  ";https://www.googleapis.com/auth/drive.scripts" +
                  ";https://www.googleapis.com/auth/drive.apps.readonly" +
                  ";https://www.googleapis.com/auth/drive.metadata.readonly" +
                  ";https://www.googleapis.com/auth/drive.file" +
                  ";https://www.googleapis.com/auth/plus.circles.read");
      }
    });
    return registry;
  }

  /**
   * Singleton data access object providing access to connections across all
   * users.
   */
  @Bean
  public UsersConnectionRepository usersConnectionRepository (ConnectionFactoryLocator connectionFactoryLocator) {
    InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository (connectionFactoryLocator);
    repository.setConnectionSignUp (new SimpleConnectionSignUp ());
    return repository;
  }

  /**
   * Request-scoped data access object providing access to the current user's
   * connections.
   */
  @Bean
  @Scope (value = "request", proxyMode = ScopedProxyMode.INTERFACES)
  public ConnectionRepository connectionRepository (UsersConnectionRepository usersConnectionRepository) {
    return usersConnectionRepository.createConnectionRepository (SecurityContext.getCurrentUser ().getId ());
  }

  /**
   * A proxy to a request-scoped object representing the current user's primary
   * Google account.
   * 
   * @throws NotConnectedException if the user is not connected to Google.
   */
  @Bean
  @Scope (value = "request", proxyMode = ScopedProxyMode.INTERFACES)
  public Google google (ConnectionRepository connectionRepository) {
    return connectionRepository.getPrimaryConnection (Google.class).getApi ();
  }

  @Bean
  public SimpleSignInAdapter simpleSignInAdapter () {
    return new SimpleSignInAdapter ();
  }

  /**
   * The Spring MVC Controller that allows users to sign-in with their provider
   * accounts.
   */
  @Bean
  public ProviderSignInController providerSignInController (UsersConnectionRepository usersConnectionRepository,
                                                            SimpleSignInAdapter simpleSignInAdapter) {
    ProviderSignInController controller = new ProviderSignInController (connectionFactoryLocator (),
                                                                        usersConnectionRepository,
                                                                        simpleSignInAdapter);
    controller.addSignInInterceptor (new ProviderSignInInterceptor<Google> () {
      @Override
      public void preSignIn (ConnectionFactory<Google> connectionFactory,
                             MultiValueMap<String, String> parameters,
                             WebRequest request) {
        parameters.add ("access_type", "offline");
      }

      @Override
      public void postSignIn (Connection<Google> connection, WebRequest request) {}
    });
    return controller;
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #addInterceptors
   * (org.springframework.web.servlet.config.annotation.InterceptorRegistry) */
  @Override
  public void addInterceptors (InterceptorRegistry registry) {
    registry.addInterceptor (new UserInterceptor (usersConnectionRepository (connectionFactoryLocator ())));
  }
}
