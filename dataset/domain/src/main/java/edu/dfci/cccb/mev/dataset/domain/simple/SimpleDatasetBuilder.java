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
package edu.dfci.cccb.mev.dataset.domain.simple;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import edu.dfci.cccb.mev.dataset.domain.contract.Analyses;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDatasetBuilder;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode (callSuper = true)
@ToString
public class SimpleDatasetBuilder extends AbstractDatasetBuilder {

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDatasetBuilder#aggregate
   * (java.lang.String, edu.dfci.cccb.mev.dataset.domain.contract.Values,
   * edu.dfci.cccb.mev.dataset.domain.contract.Analyses,
   * edu.dfci.cccb.mev.dataset.domain.contract.Dimension[]) */
  @Override
  protected Dataset aggregate (String name, Values values, Analyses analyses, Dimension... dimensions) throws DatasetBuilderException,
                                                                                                      InvalidDatasetNameException {
    return new SimpleDataset (name, values, analyses, dimensions);
  }
}
