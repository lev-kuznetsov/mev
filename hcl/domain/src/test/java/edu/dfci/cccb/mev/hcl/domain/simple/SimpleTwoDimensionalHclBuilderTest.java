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
package edu.dfci.cccb.mev.hcl.domain.simple;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.hcl.domain.contract.Branch;
import edu.dfci.cccb.mev.hcl.domain.contract.Leaf;
import edu.dfci.cccb.mev.hcl.domain.contract.Linkage;
import edu.dfci.cccb.mev.hcl.domain.contract.Metric;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;
import edu.dfci.cccb.mev.hcl.domain.mock.MockNodeBuilder;

public class SimpleTwoDimensionalHclBuilderTest {

  @Test
  public void cluster () throws Exception {
    Dataset ds = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
                                            .setValueStoreBuilder (new MapBackedValueStoreBuilder ())
                                            .build (new MockTsvInput ("mock",
                                                                      "id\tsa\tsb\tsc\n" +
                                                                              "g1\t.1\t.2\t.3\n" +
                                                                              "g2\t.4\t.5\t.6"));
    Node root = new SimpleTwoDimensionalHclBuilder ().nodeBuilder (new MockNodeBuilder ())
                                                     .dataset (ds)
                                                     .dimension (ds.dimension (Type.COLUMN))
                                                     .linkage (Linkage.AVERAGE)
                                                     .metric (Metric.EUCLIDEAN).build ().root ();
    Branch b = (Branch) root;
    Iterator<Node> i = b.children ().iterator ();
    Node l = i.next ();
    Node r = i.next ();
    Leaf t = (l instanceof Leaf) ? (Leaf) l : (Leaf) r;
    assertThat (t.name (), is ("sa"));
  }
}
