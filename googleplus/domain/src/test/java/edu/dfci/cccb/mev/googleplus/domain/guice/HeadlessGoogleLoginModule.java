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

import static com.google.inject.util.Modules.override;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.AUTHORIZATION_URL;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.REDIRECT_URI;
import static java.lang.Thread.sleep;
import static org.openqa.selenium.By.id;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

/**
 * @author levk
 */
public class HeadlessGoogleLoginModule implements Module {

  public static final String EMAIL = "google.test.email";
  public static final String PASSWORD = "google.test.password";

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    binder.install (override (new GoogleDomainModule () {
      @Override
      public void configure (ScopeBinder binder) {
        HeadlessGoogleLoginModule.this.configure (binder);
      }
    }).with (new Module () {
      private static final String EMAIL_BOX_ID = "Email";
      private static final String PASSWORD_BOX_ID = "Passwd";
      private static final String SIGNIN_BUTTON_ID = "signIn";
      private static final String APPROVE_BUTTON_ID = "submit_approve_access";
      private static final String CODE_BOX_ID = "code";

      @Provides
      @Singleton
      public Capabilities capabilities () {
        DesiredCapabilities capabilities = new DesiredCapabilities ();
        capabilities.setJavascriptEnabled (true);
        return capabilities;
      }

      @Provides
      @Singleton
      public WebDriver driver (Capabilities capabilities) {
        return new HtmlUnitDriver (capabilities);
      }

      @Provides
      @Singleton
      public GoogleCredential credential (@Named (EMAIL) String email,
                                          @Named (PASSWORD) String password,
                                          @Named (REDIRECT_URI) String redirectUri,
                                          @Named (AUTHORIZATION_URL) URL authorizationUrl,
                                          GoogleAuthorizationCodeFlow flow,
                                          WebDriver driver) throws FailingHttpStatusCodeException,
                                                           IOException, InterruptedException {
        synchronized (driver) {
          driver.get (authorizationUrl.toString ());

          WebElement emailBox = driver.findElement (id (EMAIL_BOX_ID));
          emailBox.clear ();
          emailBox.sendKeys (email);

          WebElement passwordBox = driver.findElement (id (PASSWORD_BOX_ID));
          passwordBox.clear ();
          passwordBox.sendKeys (password);

          driver.findElement (id (SIGNIN_BUTTON_ID)).click ();

          sleep (2000);

          driver.findElement (id (APPROVE_BUTTON_ID)).click ();

          sleep (2000);

          String code = driver.findElement (id (CODE_BOX_ID)).getAttribute ("value");

          GoogleCredential result = new GoogleCredential ();

          result.setFromTokenResponse (flow.newTokenRequest (code)
                                           .setRedirectUri (redirectUri)
                                           .execute ());

          System.out.println ("AUTHED USING CODE " + code);

          return result;
        }
      }

      @Override
      public void configure (Binder binder) {}
    }));
  }

  /**
   * @see {@link GoogleDomainModule}
   * @param binder
   */
  public void configure (ScopeBinder binder) {}
}
