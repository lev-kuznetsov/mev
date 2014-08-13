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

package edu.dfci.cccb.mev.googleplus.services.controllers;

import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.REDIRECT_URI;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

/**
 * @author levk
 * @since CRYSTAL
 */
@Path ("/google")
public class AuthorizationController {

  private @Inject GoogleAuthorizationCodeFlow flow;
  private @Inject @Named (REDIRECT_URI) String uri;
  private @Inject Provider<GoogleCredential> credential;

  @GET
  @Path ("/callback")
  public void login (@QueryParam ("code") String code) throws IOException {
    credential.get ().setFromTokenResponse (flow.newTokenRequest (code).setRedirectUri ("/").execute ());
  }

  @GET
  @Path ("/logout")
  public void logout () {}
}
