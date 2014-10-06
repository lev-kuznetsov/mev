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

package edu.dfci.cccb.mev.dataset.domain;

/**
 * @author levk
 * @since CRYSTAL
 */
public class AnalysisConfigurationException extends AnalysisInvocationException {
  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public AnalysisConfigurationException () {}

  /**
   * @param message
   */
  public AnalysisConfigurationException (String message) {
    super (message);
  }

  /**
   * @param cause
   */
  public AnalysisConfigurationException (Throwable cause) {
    super (cause);
  }

  /**
   * @param message
   * @param cause
   */
  public AnalysisConfigurationException (String message, Throwable cause) {
    super (message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public AnalysisConfigurationException (String message,
                                         Throwable cause,
                                         boolean enableSuppression,
                                         boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }

  public AnalysisConfigurationException missing (String... arguments) {
    return property ("missing", arguments);
  }
}
