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

package edu.dfci.cccb.mev.googleplus.domain.support;

import static com.google.api.client.http.HttpStatusCodes.STATUS_CODE_UNAUTHORIZED;
import static edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule.AUTHORIZATION_URL;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.log4j.Log4j;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;

import edu.dfci.cccb.mev.common.domain.MevException;
import edu.dfci.cccb.mev.googleplus.domain.contract.UnauthorizedException;

/**
 * @author levk
 * @since CRYSTAL
 */
@Log4j
public final class Mixins {

  private static @Inject @Named (AUTHORIZATION_URL) URL authorizationUrl;

  /**
   * Wraps request.execute() to provide better exception handling
   * 
   * @param request
   * @return
   * @throws MevException
   * @throws IOException
   */
  public static <T, R extends AbstractGoogleClientRequest<T>> T execute (R request) throws MevException, IOException {
    try {
      return request.execute ();
    } catch (GoogleJsonResponseException e) {
      switch (e.getStatusCode ()) {
      case STATUS_CODE_UNAUTHORIZED:
        throw new UnauthorizedException (e).authorizationUrl (authorizationUrl);
      }
      log.debug ("Unhandled google exception", e);
      throw e;
    }
  }

  private Mixins () {}
}
