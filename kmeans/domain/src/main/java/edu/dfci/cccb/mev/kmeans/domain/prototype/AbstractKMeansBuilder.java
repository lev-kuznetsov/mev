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
package edu.dfci.cccb.mev.kmeans.domain.prototype;

import static lombok.AccessLevel.PROTECTED;

import javax.inject.Inject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysisBuilder;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeans;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeansBuilder;

/**
 * @author levk
 * 
 */
@Accessors (fluent = true, chain = true)
@ToString
@EqualsAndHashCode (callSuper = true)
public abstract class AbstractKMeansBuilder extends AbstractAnalysisBuilder<KMeansBuilder, KMeans> implements KMeansBuilder {

  private @Getter (PROTECTED) @Setter @Inject Dataset dataset;
  private @Getter (PROTECTED) @Setter @Inject Dimension dimension;
  private @Getter @Setter int iterations = 100;
  private @Getter @Setter double convergence = 0.001;
  private @Getter @Setter int k = -1;

  /**
   * @param type
   */
  protected AbstractKMeansBuilder () {
    super ("K-means Clustering");
  }
}
