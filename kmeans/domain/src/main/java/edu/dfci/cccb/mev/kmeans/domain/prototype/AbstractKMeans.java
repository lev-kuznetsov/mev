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

import java.util.Set;

import javax.inject.Inject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeans;
import edu.dfci.cccb.mev.kmeans.domain.simple.SimpleFlatClusteredDimension;

/**
 * @author levk
 * 
 */
@ToString (exclude = "dataset")
@EqualsAndHashCode (callSuper = true)
@Accessors (fluent = true, chain = true)
public abstract class AbstractKMeans extends AbstractAnalysis<AbstractKMeans> implements KMeans {

  private @Getter @Setter Set<Set<String>> clusters;
  private @Setter Dimension dimension;
  private @Getter @Setter Dataset dataset;
  private @Inject ObjectMapper mapper;

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.kmeans.contract.KMeans#apply() */
  @Override
  public Dimension apply () throws InvalidDimensionTypeException {
    Dimension result = new SimpleFlatClusteredDimension (dimension.type (),
                                                         clusters,
                                                         dimension.selections (),
                                                         dimension.annotation ());
    dataset.set (result);
    return result;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.kmeans.domain.contract.KMeans#dimension() */
  @Override
  public Type dimension () {
    return dimension.type ();
  }
}
