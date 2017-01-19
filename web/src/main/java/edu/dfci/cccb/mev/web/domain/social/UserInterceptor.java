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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.google.api.Google;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.dfci.cccb.mev.dataset.rest.google.SecurityContext;
import edu.dfci.cccb.mev.dataset.rest.google.User;

/**
 * @author levk
 * 
 */
public class UserInterceptor extends HandlerInterceptorAdapter {

  private final UserCookieGenerator userCookieGenerator = new UserCookieGenerator ();
  private final UsersConnectionRepository connectionRepository;

  public UserInterceptor (UsersConnectionRepository connectionRepository) {
    this.connectionRepository = connectionRepository;
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
   * (javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse, java.lang.Object) */
  @Override
  public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String userId = userCookieGenerator.readCookieValue (request);
    if (userId != null) {
      if (connectionRepository.createConnectionRepository (userId).findPrimaryConnection (Google.class) != null)
        SecurityContext.setCurrentUser (new User (userId));
      else
        userCookieGenerator.removeCookie (response);
    }
    return true;
  }

  @Override
  public void postHandle (HttpServletRequest request,
                          HttpServletResponse response,
                          Object handler,
                          ModelAndView modelAndView) throws Exception {
    SecurityContext.remove ();
  }
}
