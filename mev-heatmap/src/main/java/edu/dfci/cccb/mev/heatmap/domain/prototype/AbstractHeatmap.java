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
package edu.dfci.cccb.mev.heatmap.domain.prototype;

import lombok.Getter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;

/**
 * @author levk
 * 
 */
@Accessors (fluent = true)
public abstract class AbstractHeatmap implements Heatmap {

  private @Getter String name;

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Heatmap#rename(java.lang.String) */
  @Override
  public void rename (String name) {
    this.name = name;
  }

  public static abstract class AbstractBuilder implements Builder {}
}
