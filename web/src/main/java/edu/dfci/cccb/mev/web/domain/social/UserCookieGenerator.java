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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.CookieGenerator;

/**
 * @author levk
 * 
 */
public class UserCookieGenerator {

  private final CookieGenerator userCookieGenerator = new CookieGenerator ();

  public UserCookieGenerator () {
    userCookieGenerator.setCookieName ("quickstart_user");
  }

  public void addCookie (String userId, HttpServletResponse response) {
    userCookieGenerator.addCookie (response, userId);
  }

  public void removeCookie (HttpServletResponse response) {
    userCookieGenerator.addCookie (response, "");
  }

  public String readCookieValue (HttpServletRequest request) {
    Cookie[] cookies = request.getCookies ();
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (cookie.getName ().equals (userCookieGenerator.getCookieName ())) {
        return cookie.getValue ();
      }
    }
    return null;
  }
}
