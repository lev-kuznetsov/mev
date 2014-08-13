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

import static com.google.inject.name.Names.named;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.API_KEY;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.API_SECRET;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.REDIRECT_URI;
import static edu.dfci.cccb.mev.googleplus.domain.guice.ScopeBinder.Scopes.DRIVE;
import static edu.dfci.cccb.mev.googleplus.domain.guice.ScopeBinder.Scopes.EMAIL;
import static edu.dfci.cccb.mev.googleplus.domain.guice.ScopeBinder.Scopes.PROFILE;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author levk
 */
public class GoogleDomainModuleTest {

  public void t () throws Exception {
    try {
      Injector injector = Guice.createInjector (new AbstractModule () {

        @Override
        protected void configure () {
          bind (String.class).annotatedWith (named (API_KEY))
                             .toInstance ("646144882022-6oebl3fdcg0d1l5b5trpvqfbrhhqfdpj.apps.googleusercontent.com");
          bind (String.class).annotatedWith (named (API_SECRET)).toInstance ("U6E415LFFxoDteqVCYkRtaQy");
          bind (String.class).annotatedWith (named (REDIRECT_URI)).toInstance ("urn:ietf:wg:oauth:2.0:oob");
        }
      }, new GoogleDomainModule () {
        public void configure (ScopeBinder binder) {
          binder.request (PROFILE, EMAIL, DRIVE);
        }
      });
      injector.getInstance (GoogleCredential.class)
              .setFromTokenResponse (injector.getInstance (GoogleAuthorizationCodeFlow.class)
                                             .newTokenRequest ("4/F9NrM300XWPnCnrCaIHCnVgUTLFU.Ym5debebujER3oEBd8DOtNAX6UuNjwI")
                                             .setRedirectUri ("urn:ietf:wg:oauth:2.0:oob")
                                             .execute ());
      injector.getInstance (Drive.class).files ().insert (new File ());
    } catch (Exception e) {
      e.printStackTrace ();
      System.err.println (e);
    }
  }
}
