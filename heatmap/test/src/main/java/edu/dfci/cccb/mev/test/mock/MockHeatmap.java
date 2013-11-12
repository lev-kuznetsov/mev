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
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractHeatmap;

/**
 * @author levk
 *
 */
public class MockHeatmap extends AbstractHeatmap {

  private final Annotation columnAnnotation;
  /**
   * @param name
   */
  public MockHeatmap (String name) {
    super (name);
    columnAnnotation=null;
  }
  
  public MockHeatmap(String name, Annotation columnAnnotation){
    super (name);
    this.columnAnnotation=columnAnnotation;
    
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
    
    return columnAnnotation;
  }
}
