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
package edu.dfci.cccb.mev.domain;

import java.util.AbstractList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author levk
 * 
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class MatrixData {

  /**
   * Matrix data
   */
  private final RealMatrix data;

  /**
   * Gets number of rows
   * 
   * @return
   */
  public int rows () {
    return data.getRowDimension ();
  }

  /**
   * Gets number of columns
   * 
   * @return
   */
  public int columns () {
    return data.getColumnDimension ();
  }

  /**
   * Gets the matrix values as a single dimmensional array row by row
   * 
   * @return
   */
  public List<Double> values () {
    return new AbstractList<Double> () {

      /* (non-Javadoc)
       * @see java.util.AbstractList#get(int)
       */
      @Override
      public Double get (int index) {
        try {
          return data.getEntry (index / data.getColumnDimension (), index % data.getColumnDimension ());
        } catch (OutOfRangeException e) {
          throw new IndexOutOfBoundsException ();
        }
      }

      /* (non-Javadoc)
       * @see java.util.AbstractCollection#size()
       */
      @Override
      public int size () {
        return data.getColumnDimension () * data.getRowDimension ();
      }
    };
  }
}
