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
package edu.dfci.cccb.mev.dataset.domain.simple;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValueStoreBuilderFactory;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode (callSuper = true)
@ToString
public class ClassValueStoreBuilderFactory <T extends ValueStoreBuilder> extends AbstractValueStoreBuilderFactory {

  private final Class<T> valueBuilderClass;

  /**
   * 
   */
  public ClassValueStoreBuilderFactory (Class<T> valueBuilderClass) {
    this.valueBuilderClass = valueBuilderClass;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilderFactory#builder
   * () */
  @Override
  public ValueStoreBuilder builder () throws DatasetBuilderException {
    try {
      return valueBuilderClass.newInstance ();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new DatasetBuilderException (e);
    }
  }
}
