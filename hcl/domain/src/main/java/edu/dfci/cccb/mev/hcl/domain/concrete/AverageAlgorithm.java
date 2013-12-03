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
package edu.dfci.cccb.mev.hcl.domain.concrete;

import edu.dfci.cccb.mev.hcl.domain.prototype.AbstractAlgorithm;

/**
 * @author levk
 * 
 */
public class AverageAlgorithm extends AbstractAlgorithm {

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.hcl.domain.contract.Algorithm#aggregate(java.lang.Iterable
   * ) */
  @Override
  public double aggregate (Iterable<Double> distances) {
    int count = 0;
    double aggregate = .0;
    for (double distance : distances)
      aggregate += distance / ++count;
    return aggregate;
  }
}
