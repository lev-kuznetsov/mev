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

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Synchronized;
import lombok.experimental.Accessors;

/**
 * @author levk
 * 
 */
@SuppressWarnings ("unchecked")
@Accessors (fluent = true)
public abstract class MevException extends Exception {
  private static final long serialVersionUID = 1L;

  private @Getter String code;
  private @Getter final Map<String, String> arguments = new HashMap<String, String> ();

  {
    code ("mev.unspecified");
  }

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

  public <T extends MevException> T code (String code) {
    this.code = code;
    return (T) this;
  }

  @Synchronized
  public <T extends MevException> T argument (String key, String value) {
    arguments.put (key, value);
    return (T) this;
  }

  /* (non-Javadoc)
   * @see java.lang.Throwable#toString() */
  @Override
  public String toString () {
    return super.toString () + " arguments: " + arguments;
  }
}
