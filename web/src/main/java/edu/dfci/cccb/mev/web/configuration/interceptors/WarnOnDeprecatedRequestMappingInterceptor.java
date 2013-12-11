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
package edu.dfci.cccb.mev.web.configuration.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author levk
 * 
 */
@Log4j
public class WarnOnDeprecatedRequestMappingInterceptor extends HandlerInterceptorAdapter {

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
   * (javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse, java.lang.Object) */
  @Override
  public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (handler instanceof HandlerMethod) {
      HandlerMethod method = (HandlerMethod) handler;
      if (method.getMethodAnnotation (Deprecated.class) != null
          || method.getMethod ().getDeclaringClass ().isAnnotationPresent (Deprecated.class))
        log.warn ("Use of deprecated API on URI " + request.getRequestURI () + " dispatched to "
                  + method.getMethod ().getDeclaringClass ().getSimpleName () + "." + method.getMethod ().getName ()
                  + "()");
    }
    return true;
  }
}
