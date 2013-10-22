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
package edu.dfci.cccb.mev.heatmap.domain;

/**
 * @author levk
 *
 */
public class ResourceNotFoundException extends HeatmapException {
  private static final long serialVersionUID = 1L;

  {
    code ("heatmap.resource.not.found");
  }

  /**
   * 
   */
  protected ResourceNotFoundException () {
    super ();
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  protected ResourceNotFoundException (String message,
                                       Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }

  /**
   * @param message
   * @param cause
   */
  protected ResourceNotFoundException (String message, Throwable cause) {
    super (message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   */
  protected ResourceNotFoundException (String message) {
    super (message);
  }

  /**
   * @param cause
   */
  protected ResourceNotFoundException (Throwable cause) {
    super (cause);
  }
}
