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

package edu.dfci.cccb.mev.googleplus.domain.contract;

import java.net.URL;

import edu.dfci.cccb.mev.common.domain.support.MevException;

/**
 * @author levk
 */
public class UnauthorizedException extends MevException {
  private static final long serialVersionUID = 1L;

  public static final String AUTHORIZATION_URL = "authorization.url";

  /**
   * 
   */
  public UnauthorizedException () {}

  /**
   * @param message
   */
  public UnauthorizedException (String message) {
    super (message);
  }

  /**
   * @param cause
   */
  public UnauthorizedException (Throwable cause) {
    super (cause);
  }

  /**
   * @param message
   * @param cause
   */
  public UnauthorizedException (String message, Throwable cause) {
    super (message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public UnauthorizedException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }

  /**
   * Sets authorization URL
   * 
   * @param url
   */
  public UnauthorizedException authorizationUrl (URL url) {
    return property (AUTHORIZATION_URL, url);
  }
}
