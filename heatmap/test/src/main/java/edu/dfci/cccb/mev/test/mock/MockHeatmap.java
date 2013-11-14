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
package edu.dfci.cccb.mev.test.mock;

import edu.dfci.cccb.mev.heatmap.domain.Annotation;
import edu.dfci.cccb.mev.heatmap.domain.Data;
import edu.dfci.cccb.mev.heatmap.domain.Dimension;
import edu.dfci.cccb.mev.heatmap.domain.DimensionHeader;
import edu.dfci.cccb.mev.heatmap.domain.concrete.DimensionHeaderSimple;
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractHeatmap;

/**
 * @author levk
 *
 */
public class MockHeatmap extends AbstractHeatmap {

  private final DimensionHeader<String> columnHeader;
  private final DimensionHeader<String> rowHeader;
  
  
  /**
   * @param name
   */
  
  public MockHeatmap (String name) {
    super (name);
    this.columnHeader=new DimensionHeaderSimple<String> (Dimension.COLUMN, null);
    this.rowHeader=new DimensionHeaderSimple<String> (Dimension.ROW, null);
  }
  
  public MockHeatmap(String name, DimensionHeader<String> columnHeader){
    super (name);
    this.columnHeader=columnHeader;
    this.rowHeader=new DimensionHeaderSimple<String> (Dimension.ROW, null);
  }
  
  

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Heatmap#data()
   */
  @Override
  public Data data () {
    return null;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Heatmap#annotation()
   */
  @Override
  public Annotation annotation (Dimension dimension) {
    return dimension==Dimension.COLUMN ?  this.columnHeader.getAnnotation () : this.rowHeader.getAnnotation ();
  }
  
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Heatmap#annotation()
   */
  @Override
  public DimensionHeader<String> dimensionHeader (Dimension dimension) {
    return dimension==Dimension.COLUMN ?  this.columnHeader : this.rowHeader;
  }
  
  @Override
  public DimensionHeader<String> dimensionHeader (String dimension) {
    return dimension.equals(Dimension.COLUMN.name ()) ?  this.columnHeader : this.rowHeader;
  }
  
  @Override
  public DimensionHeader<String> columnHeader () {
    return this.columnHeader;
  }
}
