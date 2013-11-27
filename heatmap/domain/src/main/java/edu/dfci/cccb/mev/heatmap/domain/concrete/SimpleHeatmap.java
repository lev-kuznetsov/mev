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
package edu.dfci.cccb.mev.heatmap.domain.concrete;

import edu.dfci.cccb.mev.heatmap.domain.Adjuster;
import edu.dfci.cccb.mev.heatmap.domain.Annotation;
import edu.dfci.cccb.mev.heatmap.domain.Data;
import edu.dfci.cccb.mev.heatmap.domain.Dimension;
import edu.dfci.cccb.mev.heatmap.domain.Order;
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractHeatmap;

/**
 * @author levk
 * 
 */
public class SimpleHeatmap extends AbstractHeatmap {

  private Data data;
  private Annotation annotation;
  private Order order;

  /**
   * @param name
   */
  public SimpleHeatmap (String name, Data data, Order mapper) {
    super (name);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Heatmap#data() */
  @Override
  public Data data () {
    return data;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Heatmap#mapper() */
  @Override
  public Order order () {
    return order;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.heatmap.domain.Heatmap#reorder(edu.dfci.cccb.mev.heatmap
   * .domain.Adjuster) */
  @Override
  public void reorder (Adjuster adjuster) {
    order = adjuster.adjust (data);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.heatmap.domain.Heatmap#annotation(edu.dfci.cccb.mev.
   * heatmap.domain.Dimension) */
  @Override
  public Annotation annotation (Dimension dimension) {
    return annotation;
  }
}
