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

package edu.dfci.cccb.mev.googleplus.domain.guice;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.bindProperties;
import static com.google.inject.name.Names.named;
import static edu.dfci.cccb.mev.common.domain.guice.MevDomainModule.load;
import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import lombok.SneakyThrows;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Scope;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

import edu.dfci.cccb.mev.googleplus.domain.utilities.Mixins;

/**
 * @author levk
 * @since CRYSTAL
 */
public class GoogleDomainModule implements Module {

  public static final String API_KEY = "google.api.key";
  public static final String API_SECRET = "google.api.secret";
  public static final String REDIRECT_URI = "google.api.redirect.uri";
  public static final String AUTHORIZATION_URL = "google.api.redirect.url";
  public static final String SCOPES = "google.api.scopes";
  public static final String APPLICATION_NAME = "application.name";

  /**
   * Provides a transport for generic environments
   */
  @Provides
  @Singleton
  public HttpTransport transport () {
    return new NetHttpTransport ();
  }

  /**
   * Provides a json factory
   */
  @Provides
  @Singleton
  public JsonFactory json () {
    return new JacksonFactory ();
  }

  /**
   * Provides an authorization code flow
   */
  @Provides
  @Singleton
  public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow (HttpTransport httpTransport,
                                                                  JsonFactory jsonFactory,
                                                                  @Named (API_KEY) String key,
                                                                  @Named (API_SECRET) String secret,
                                                                  @Named (SCOPES) Set<String> scopes) {
    return new Builder (httpTransport, jsonFactory, key, secret, scopes).setAccessType ("online")
                                                                        .setApprovalPrompt ("auto")
                                                                        .build ();
  }

  /**
   * Authorization URL
   */
  @Provides
  @Singleton
  @Named (AUTHORIZATION_URL)
  @SneakyThrows (MalformedURLException.class)
  public URL login (GoogleAuthorizationCodeFlow flow, @Named (REDIRECT_URI) String redirectUri) {
    return new URL (flow.newAuthorizationUrl ().setRedirectUri (redirectUri).build ());
  }

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public final void configure (final Binder binder) {
    bindProperties (binder, load ("/META-INF/configuration/google.properties"));

    final Multibinder<String> scopes = newSetBinder (binder, String.class, named (SCOPES));

    configure (new ScopeBinder () {

      @Override
      public void request (ScopeBinder.Scope... scopes) {
        request (asList (scopes));
      }

      @Override
      public void request (Iterable<ScopeBinder.Scope> scopez) {
        for (ScopeBinder.Scope scope : scopez)
          scopes.addBinding ().toInstance (scope.value ());
      }
    });

    configure (new ServicesScopeBindingBuilder () {

      private final Collection<ScopedBindingBuilder> services;

      {
        services = new ArrayList<> ();
        services.add (binder.bind (GoogleCredential.class));
        services.add (binder.bind (Drive.class).toProvider (new com.google.inject.Provider<Drive> () {

          private @Inject Provider<GoogleCredential> credentialProvider;
          private @Inject HttpTransport transport;
          private @Inject JsonFactory jsonFactory;
          private @com.google.inject.Inject (optional = true) @Named (APPLICATION_NAME) String applicationName = "MeV";

          @Override
          public Drive get () {
            return new Drive.Builder (transport,
                                      jsonFactory,
                                      credentialProvider.get ()).setApplicationName (applicationName)
                                                                .build ();
          }
        }));
      }

      @Override
      public void in (Scope scope) {
        for (ScopedBindingBuilder service : services)
          service.in (scope);
      }

      @Override
      public void in (Class<? extends Annotation> scopeAnnotation) {
        for (ScopedBindingBuilder service : services)
          service.in (scopeAnnotation);
      }

      @Override
      public void asEagerSingleton () {
        for (ScopedBindingBuilder service : services)
          service.asEagerSingleton ();
      }
    });

    binder.requestStaticInjection (Mixins.class);
  }

  /**
   * Configures Google API scopes
   */
  public void configure (ScopeBinder binder) {}

  /**
   * Configures Google services injectables scopes
   */
  public void configure (ServicesScopeBindingBuilder bind) {
    bind.in (Singleton.class);
  }
}
