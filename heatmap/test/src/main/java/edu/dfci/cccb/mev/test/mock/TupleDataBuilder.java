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

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.dfci.cccb.mev.heatmap.domain.Data;
import edu.dfci.cccb.mev.heatmap.domain.DataException;
import edu.dfci.cccb.mev.heatmap.domain.DataProvider;
import edu.dfci.cccb.mev.heatmap.domain.Dimension;
import edu.dfci.cccb.mev.heatmap.domain.Projection;
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractData;
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractDataBuilder;

/**
 * @author levk
 * 
 */
public class TupleDataBuilder extends AbstractDataBuilder {

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.heatmap.domain.DataBuilder#build(edu.dfci.cccb.mev.heatmap
   * .domain.DataProvider) */
  @Override
  public Data build (DataProvider<?> provider) throws DataException {
    final Map<Dimension, Set<String>> ids = new HashMap<> ();
    final Map<Collection<Projection>, Double> values = new HashMap<> ();

    while (provider.next ()) {
      for (Projection projection : provider.projections ()) {
        Set<String> forDimension = ids.get (projection.dimension ());
        if (forDimension == null)
          ids.put (projection.dimension (), forDimension = new HashSet<> ());
        forDimension.add (projection.id ());
      }
      values.put (asList (provider.projections ()), (Double) provider.value ());
    }

    return new AbstractData () {

      @Override
      public Set<String> ids (Dimension dimension) {
        return ids.get (dimension);
      }

      @Override
      public double get (Projection... projections) throws DataException {
        return values.get (asList (projections));
      }
    };
  }
}
