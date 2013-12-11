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

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static org.apache.commons.math3.util.FastMath.pow;
import static org.apache.commons.math3.util.FastMath.sqrt;

import java.util.List;

import lombok.ToString;
import edu.dfci.cccb.mev.hcl.domain.prototype.AbstractMetric;

/**
 * @author levk
 * 
 */
@ToString
public class EuclideanMetric extends AbstractMetric {

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.hcl.domain.contract.Metric#distance(java.util.List,
   * java.util.List) */
  @Override
  public double distance (List<Double> from, List<Double> to) {
    double sumOfSquares = .0;
    for (int index = from.size (); --index >= 0;) {
      double f = from.get (index), t = to.get (index);
      if (!isInfinite (f) && !isInfinite (t) && !isNaN (f) && !isNaN (t))
        sumOfSquares += pow (from.get (index) - to.get (index), 2);
    }
    return sqrt (sumOfSquares);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.hcl.domain.contract.Metric#name() */
  @Override
  public String name () {
    return "euclidean";
  }
}
