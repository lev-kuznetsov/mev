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
package edu.dfci.cccb.mev.dataset.domain.mock;

import java.util.HashMap;
import java.util.Map;

import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValues.Coordinate;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValueStoreBuilder;

/**
 * @author levk
 * 
 */
public class MapBackedValueStoreBuilder extends AbstractValueStoreBuilder {

  private Map<Coordinate, Double> values = new HashMap<> ();

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder#add(double,
   * java.lang.String, java.lang.String) */
  @Override
  public ValueStoreBuilder add (double value, String row, String column) {
    values.put (new Coordinate (row, column), value);
    return this;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder#build() */
  @Override
  public Values build () {
    return new MapBackedValues (new HashMap<> (values));
  }
}
