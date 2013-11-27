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
package edu.dfci.cccb.mev.math.commons.mock;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import edu.dfci.cccb.mev.math.commons.contract.MatrixBuilderException;
import edu.dfci.cccb.mev.math.commons.prototype.AbstractRealMatrixBuilder;

/**
 * @author levk
 * 
 */
public class Array2DRealMatrixBuilder extends AbstractRealMatrixBuilder {

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.math.commons.contract.RealMatrixBuilder#build(int,
   * int) */
  @Override
  public RealMatrix build (int height, int width) throws MatrixBuilderException {
    return new Array2DRowRealMatrix (height, width);
  }
}
