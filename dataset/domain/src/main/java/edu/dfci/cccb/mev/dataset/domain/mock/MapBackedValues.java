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

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValues;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode (callSuper = true)
@ToString
public class MapBackedValues extends AbstractValues {

  @EqualsAndHashCode
  @RequiredArgsConstructor
  @Data
  public static class Coordinate {
    private final String row;
    private final String column;
  }

  private final Map<Coordinate, Double> values;
  
  @Override
  public boolean skipJson() {
    return false;
  };
  
  /**
   * 
   */
  public MapBackedValues (Map<Coordinate, Double> values) {
    this.values = values;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.Values#get(java.lang.String,
   * java.lang.String) */
  @Override
  public double get (String row, String column) throws InvalidCoordinateException {
    Double value = values.get (new Coordinate (row, column));
    if (value == null)
      throw new InvalidCoordinateException (); // TODO: add arguments
    return value;
  }
}
