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

import static java.util.Collections.emptyList;

import java.util.AbstractList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.RealMatrix;

import com.fasterxml.jackson.annotation.JsonView;

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

  @Data
  @Accessors (fluent = true, chain = true)
  public class MatrixValue {

    private final @JsonView int row;
    private final @JsonView int column;
    private final @JsonView double value;
  }

  /**
   * Gets the matrix values as a single dimmensional array row by row
   * 
   * @return
   */
  @JsonView
  public List<MatrixValue> values () {
    return new AbstractList<MatrixValue> () {

      /* (non-Javadoc)
       * @see java.util.AbstractList#get(int) */
      @Override
      public MatrixValue get (int index) {
        try {
          return new MatrixValue (index / data.getColumnDimension (),
                                  index % data.getColumnDimension (),
                                  data.getEntry (index / data.getColumnDimension (), index % data.getColumnDimension ()));
        } catch (OutOfRangeException e) {
          throw new IndexOutOfBoundsException ();
        }
      }

      /* (non-Javadoc)
       * @see java.util.AbstractCollection#size() */
      @Override
      public int size () {
        return data.getColumnDimension () * data.getRowDimension ();
      }
    };
  }
  
  public static final MatrixData EMPTY_MATRIX_DATA = new MatrixData (null) {

    @Override
    public List<MatrixValue> values () {
      return emptyList ();
    }
  };
}
