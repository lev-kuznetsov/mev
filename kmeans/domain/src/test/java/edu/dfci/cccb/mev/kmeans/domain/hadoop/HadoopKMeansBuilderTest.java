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
package edu.dfci.cccb.mev.kmeans.domain.hadoop;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeans;

/**
 * @author levk
 * 
 */
public class HadoopKMeansBuilderTest {

  private SimpleDatasetBuilder datasetBuilder;

  @Before
  public void initializeBuilder () {
    datasetBuilder = new SimpleDatasetBuilder ();
    datasetBuilder.setParserFactories (asList (new SuperCsvParserFactory ()));
    datasetBuilder.setValueStoreBuilder (new MapBackedValueStoreBuilder ());
  }

  @Test
  public void simple () throws Exception {
    Dataset d = datasetBuilder.build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\tsd\n" +
                                                                "g1\t.1\t.2\t.3\t.4\n" +
                                                                "g2\t.4\t.5\t.6\t.7"));
    assertEquals ("mock", d.name ());
    assertEquals (0.1, d.values ().get ("g1", "sa"), 0.0);
    KMeans k = new HadoopKMeansBuilder ().k (2).dataset (d).dimension (d.dimension (Type.COLUMN)).build ();
    assertEquals (new HashSet<Set<String>> (asList (new HashSet<String> (asList ("sa", "sb")),
                                                    new HashSet<String> (asList ("sc", "sd")))),
                  k.clusters ());
  }
}
