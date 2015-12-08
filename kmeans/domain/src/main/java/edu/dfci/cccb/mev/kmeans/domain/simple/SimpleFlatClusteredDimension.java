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
package edu.dfci.cccb.mev.kmeans.domain.simple;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Annotation;
import edu.dfci.cccb.mev.dataset.domain.contract.Selections;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDimension;
import edu.dfci.cccb.mev.kmeans.domain.contract.Cluster;

/**
 * @author levk
 * 
 */
@Accessors (fluent = true)
@ToString
public class SimpleFlatClusteredDimension extends AbstractDimension {

  private @Getter final Set<Cluster> clusters;
  private @Getter final List<String> keys;

  /**
   * @param type
   */
  public SimpleFlatClusteredDimension (Type type,
                                       final Set<Cluster> clusters,
                                       Selections selections,
                                       Annotation annotation) {
    super (type);
    selections (selections);
    annotation (annotation);
    this.clusters = clusters;
    this.keys = unmodifiableList (new ArrayList<String> () {
      private static final long serialVersionUID = 1L;

      {
        for (Set<String> cluster : clusters)
          addAll (cluster);
      }
    });
  }
}
