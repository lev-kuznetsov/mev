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

import static java.lang.String.valueOf;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;

/**
 * @author levk
 * 
 */
public class ValueStoreException extends DatasetBuilderException {
  private static final long serialVersionUID = 1L;

  {
    code ("value.store");
  }

  public ValueStoreException projection (Type type, String projection) {
    return argument (type.name (), projection);
  }

  public ValueStoreException value (double value) {
    return argument ("value", valueOf (value));
  }

  /**
   * 
   */
  public ValueStoreException () {}

  /**
   * @param message
   */
  public ValueStoreException (String message) {
    super (message);
  }

  /**
   * @param cause
   */
  public ValueStoreException (Throwable cause) {
    super (cause);
  }

  /**
   * @param message
   * @param cause
   */
  public ValueStoreException (String message, Throwable cause) {
    super (message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public ValueStoreException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }
}
