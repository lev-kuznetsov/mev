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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;

/**
 * @author levk
 * 
 */
public class SimpleDatasetBuilderTest {

  private SimpleDatasetBuilder builder;

  @Before
  public void initializeBuilder () {
    builder = new SimpleDatasetBuilder ();
    builder.setParserFactories (asList (new SuperCsvParserFactory ()));
    builder.setValueStoreBuilderFactory (new ClassValueStoreBuilderFactory<MapBackedValueStoreBuilder> (MapBackedValueStoreBuilder.class));
  }

  @Test
  public void simpleBuild () throws Exception {
    Dataset set = builder.build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\n" +
                                                           "g1\t.1\t.2\t.3\n" +
                                                           "g2\t.4\t.5\t.6"));
    assertEquals ("mock", set.name ());
    assertEquals (0.1, set.values ().get ("g1", "sa"), 0.0);
  }
}
