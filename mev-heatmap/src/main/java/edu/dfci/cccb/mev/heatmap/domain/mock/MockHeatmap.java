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
package edu.dfci.cccb.mev.heatmap.domain.mock;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.heatmap.domain.Annotation;
import edu.dfci.cccb.mev.heatmap.domain.Area;
import edu.dfci.cccb.mev.heatmap.domain.Axis;
import edu.dfci.cccb.mev.heatmap.domain.Data;
import edu.dfci.cccb.mev.heatmap.domain.InvalidDataRequestException;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractHeatmap;

/**
 * @author levk
 * 
 */
@ToString
@Accessors (fluent = true, chain = true)
public class MockHeatmap extends AbstractHeatmap {

  private Data data;
  private Annotation annotation;

  public MockHeatmap name (String name) {
    rename (name);
    return this;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.heatmap.domain.Heatmap#data(edu.dfci.cccb.mev.heatmap
   * .domain.Area) */
  @Override
  public Data data (Area area) throws InvalidDataRequestException {
    return data;
  }

  public MockHeatmap data (Data data) {
    this.data = data;
    return this;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.heatmap.domain.Heatmap#annotation(edu.dfci.cccb.mev.
   * heatmap.domain.Axis) */
  @Override
  public Annotation annotation (Axis axis) {
    return annotation;
  }

  public MockHeatmap annotation (Annotation annotation) {
    this.annotation = annotation;
    return this;
  }

  public static MockHeatmap heatmap (String name) {
    return new MockHeatmap ().name (name);
  }

  @Accessors (fluent = true, chain = true)
  public static class MockBuilder extends AbstractBuilder {

    private @Getter @Setter Heatmap content;

    /* (non-Javadoc)
     * @see edu.dfci.cccb.mev.heatmap.domain.Heatmap.Builder#build(edu.dfci.cccb.mev.heatmap.domain.Heatmap.Builder.Content)
     */
    @Override
    public Heatmap build (Content content) {
      return this.content;
    }
  }
}
