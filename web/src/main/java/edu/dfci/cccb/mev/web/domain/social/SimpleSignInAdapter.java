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
package edu.dfci.cccb.mev.web.domain.social;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author levk
 * 
 */
@Log4j
public class SimpleSignInAdapter implements SignInAdapter {

  private final UserCookieGenerator userCookieGenerator = new UserCookieGenerator ();

  public String signIn (String userId, Connection<?> connection, NativeWebRequest request) {
    log.info ("SIGNING IN " + userId);
    SecurityContext.setCurrentUser (new User (userId));
    userCookieGenerator.addCookie (userId, request.getNativeResponse (HttpServletResponse.class));
    return "/#/datasets?tab=google";
  }
}
