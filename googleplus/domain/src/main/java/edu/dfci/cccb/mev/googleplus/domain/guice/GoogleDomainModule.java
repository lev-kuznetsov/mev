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
import static edu.dfci.cccb.mev.common.domain.guice.MevModule.load;
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
import com.google.api.services.plus.Plus;
import com.google.api.services.plusDomains.PlusDomains;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Scope;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;
import edu.dfci.cccb.mev.googleplus.domain.support.Mixins;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CircleFeedSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CircleSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CommentListSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CommentReplySerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CommentSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CoverInfoSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CoverPhotoSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.CoverSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.DateTimeSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.EmailsSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.FileListSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.FileSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.ImageSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.LabelsSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.OrganizationsSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PeopleFeedSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PeopleSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PersonSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PlacesLivedSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.PropertySerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.UrlsSerializer;
import edu.dfci.cccb.mev.googleplus.domain.support.jackson.UserSerializer;

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
    return new Builder (httpTransport, jsonFactory, key, secret, scopes).setAccessType ("offline")
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

    binder.install (new JacksonModule () {
      /* (non-Javadoc)
       * @see
       * edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonModule#configure
       * (edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder) */
      @Override
      public void configure (JacksonSerializerBinder binder) {
        binder.with (CircleFeedSerializer.class);
        binder.with (CircleSerializer.class);
        binder.with (CommentListSerializer.class);
        binder.with (CommentReplySerializer.class);
        binder.with (CommentSerializer.class);
        binder.with (CoverInfoSerializer.class);
        binder.with (CoverPhotoSerializer.class);
        binder.with (CoverSerializer.class);
        binder.with (DateTimeSerializer.class);
        binder.with (EmailsSerializer.class);
        binder.with (FileListSerializer.class);
        binder.with (FileSerializer.class);
        binder.with (ImageSerializer.class);
        binder.with (LabelsSerializer.class);
        binder.with (OrganizationsSerializer.class);
        binder.with (PeopleFeedSerializer.class);
        binder.with (PeopleSerializer.class);
        binder.with (PersonSerializer.class);
        binder.with (PlacesLivedSerializer.class);
        binder.with (PropertySerializer.class);
        binder.with (UrlsSerializer.class);
        binder.with (UserSerializer.class);
      }
    });

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
      
      abstract class ContextProvider <T> implements com.google.inject.Provider<T> {
        protected @Inject HttpTransport transport;
        protected @Inject JsonFactory jsonFactory;
      }

      abstract class ServiceProvider <T> extends ContextProvider<T> {
        protected @Inject Provider<GoogleCredential> credentialProvider;
        protected @Inject @Named (APPLICATION_NAME) String applicationName;
      }

      {
        services = new ArrayList<> ();
        services.add (binder.bind (GoogleCredential.class).toProvider (new ContextProvider<GoogleCredential> () {
          private @Inject @Named (API_KEY) String key;
          private @Inject @Named (API_SECRET) String secret;

          @Override
          public GoogleCredential get () {
            return new GoogleCredential.Builder ().setJsonFactory (jsonFactory)
                                                  .setTransport (transport)
                                                  .setClientSecrets (key, secret)
                                                  .build ();
          }
        }));
        services.add (binder.bind (Drive.class).toProvider (new ServiceProvider<Drive> () {
          @Override
          public Drive get () {
            return new Drive.Builder (transport,
                                      jsonFactory,
                                      credentialProvider.get ()).setApplicationName (applicationName)
                                                                .build ();
          }
        }));
        services.add (binder.bind (Plus.class).toProvider (new ServiceProvider<Plus> () {
          @Override
          public Plus get () {
            return new Plus.Builder (transport,
                                     jsonFactory,
                                     credentialProvider.get ()).setApplicationName (applicationName)
                                                               .build ();
          }
        }));
        services.add (binder.bind (PlusDomains.class).toProvider (new ServiceProvider<PlusDomains> () {
          @Override
          public PlusDomains get () {
            return new PlusDomains.Builder (transport,
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

    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {
        binder.requestStaticInjection (Mixins.class);
      }
    });
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
