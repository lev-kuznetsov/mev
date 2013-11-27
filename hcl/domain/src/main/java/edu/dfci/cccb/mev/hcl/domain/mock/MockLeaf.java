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
package edu.dfci.cccb.mev.hcl.domain.mock;

import edu.dfci.cccb.mev.hcl.domain.prototype.AbstractLeaf;

/**
 * @author levk
 * 
 */
public class MockLeaf extends AbstractLeaf<Double> {

  private double distance;
  private String name;

  /**
   * 
   */
  public MockLeaf (double distance, String name) {
    this.name = name;
    this.distance = distance;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.hcl.domain.contract.Leaf#name() */
  @Override
  public String name () {
    return name;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.hcl.domain.contract.Node#distance() */
  @Override
  public Double distance () {
    return distance;
  }
}
