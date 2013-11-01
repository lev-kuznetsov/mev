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
package edu.dfci.cccb.mev.api.support;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;

/**
 * @author levk
 * 
 */
public abstract class MevException extends Exception {
  private static final long serialVersionUID = 1L;

  private String code = "";
  private String[] arguments = new String[] { "" };

  /**
   * 
   */
  public MevException () {}

  /**
   * @param message
   */
  public MevException (String message) {
    super (message);
  }

  /**
   * @param cause
   */
  public MevException (Throwable cause) {
    super (cause);
  }

  /**
   * @param message
   * @param cause
   */
  public MevException (String message, Throwable cause) {
    super (message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public MevException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }

  public String code () {
    return code;
  }

  @SuppressWarnings ("unchecked")
  public <T extends MevException> T code (String code) {
    this.code = code;
    return (T) this;
  }

  public String[] arguments () {
    return copyOf (arguments, arguments.length);
  }

  @SuppressWarnings ("unchecked")
  public synchronized <T extends MevException> T arguments (String first, String... rest) {
    String[] temp = new String[arguments.length + rest.length + 1];
    arraycopy (arguments, 0, temp, 0, arguments.length);
    temp[arguments.length] = first;
    if (rest.length > 0)
      arraycopy (rest, 0, temp, arguments.length + 1, rest.length);
    return (T) this;
  }
}
