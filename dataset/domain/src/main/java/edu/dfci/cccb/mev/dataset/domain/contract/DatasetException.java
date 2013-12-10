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
package edu.dfci.cccb.mev.dataset.domain.contract;


/**
 * @author levk
 * 
 */
public class DatasetException extends MevException {
  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public DatasetException () {}

  /**
   * @param message
   */
  public DatasetException (String message) {
    super (message);
  }

  /**
   * @param cause
   */
  public DatasetException (Throwable cause) {
    super (cause);
  }

  /**
   * @param message
   * @param cause
   */
  public DatasetException (String message, Throwable cause) {
    super (message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public DatasetException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }
}
