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
package edu.dfci.cccb.mev.web.domain.spring;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import edu.dfci.cccb.mev.web.domain.social.SocialContext;

/**
 * @author levk
 * 
 */
public class SpringSocialSignInAdapter implements SignInAdapter {

  private @Getter @Setter (onMethod = @_ (@Inject)) SocialContext context;

  /* (non-Javadoc)
   * @see
   * org.springframework.social.connect.web.SignInAdapter#signIn(java.lang.
   * String, org.springframework.social.connect.Connection,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public String signIn (String userId, Connection<?> connection, NativeWebRequest request) {
    context.set (userId);
    return null;
  }
}
