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
package edu.dfci.cccb.mev.dataset.rest.assembly.json.simple;

import lombok.ToString;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.prototype.AbstractDimensionJsonSerializer;

/**
 * @author levk
 * 
 */
@ToString
public class SimpleDimensionJsonSerializer extends AbstractDimensionJsonSerializer<SimpleDimension> {

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.assembly.json.prototype.
   * AbstractDimensionJsonSerializer#handledType() */
  @Override
  public Class<SimpleDimension> handledType () {
    return SimpleDimension.class;
  }
}
