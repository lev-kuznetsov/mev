/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.dataset.domain.prototype;

import static com.mycila.inject.internal.guava.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.contract.Values.Value;

public class DatasetAdapterTest {

  private DatasetAdapter<String, Double> dataset1;
  private DatasetAdapter<String, Double> dataset2;
  private DatasetAdapter<String, Double> dataset3;
  private Map<String, Dataset<String, Double>> workspace;

  @SuppressWarnings ("unchecked")
  private DatasetAdapter<String, Double> createDataset (String name) throws Exception {
    Dimension<String> row = mock (Dimension.class);
    when (row.name ()).thenReturn ("row");
    when (row.iterator ()).thenReturn (asList ("r1", "r2", "r3").iterator ());
    Dimension<String> column = mock (Dimension.class);
    when (column.name ()).thenReturn ("column");
    when (column.iterator ()).thenReturn (asList ("c1", "c2", "c3", "c4").iterator ());
    Map<String, Dimension<String>> dimensions = new LinkedHashMap<> ();
    dimensions.put ("row", row);
    dimensions.put ("column", column);
    Values<String, Double> values = mock (Values.class);
    List<Value<String, Double>> vals = asList (new Value<String, Double> (.0, of ("row", "r1", "column", "c1")),
                                               new Value<String, Double> (.1, of ("row", "r1", "column", "c2")),
                                               new Value<String, Double> (.2, of ("row", "r1", "column", "c3")),
                                               new Value<String, Double> (.3, of ("row", "r1", "column", "c4")),
                                               new Value<String, Double> (-.1, of ("row", "r2", "column", "c1")),
                                               new Value<String, Double> (-.2, of ("row", "r2", "column", "c2")),
                                               new Value<String, Double> (-.3, of ("row", "r2", "column", "c3")),
                                               new Value<String, Double> (-.4, of ("row", "r2", "column", "c4")),
                                               new Value<String, Double> (.11, of ("row", "r3", "column", "c1")),
                                               new Value<String, Double> (-.22, of ("row", "r3", "column", "c2")),
                                               new Value<String, Double> (.33, of ("row", "r3", "column", "c3")),
                                               new Value<String, Double> (-.44, of ("row", "r3", "column", "c4")));
    when (values.get ((Iterable<Map<String, String>>) anyObject ())).thenReturn (vals);
    return new DatasetAdapter<String, Double> (name,
                                               dimensions,
                                               (Map<String, Analysis>) new HashMap<String, Analysis> (),
                                               values) {};
  }

  @Before
  public void setUpDataset () throws Exception {
    dataset1 = createDataset ("mock");
    dataset2 = createDataset ("mock2");
    dataset3 = createDataset ("mock");
    workspace = DatasetAdapter.workspace ();
  }

  @Test
  public void datasetName () throws Exception {
    assertThat (dataset1.name (), equalTo ("mock"));
  }

  @Test
  public void datasetDimensions () throws Exception {
    assertThat (dataset1.dimensions ().containsKey ("row"), is (true));
    assertThat (dataset1.dimensions ().containsKey ("column"), is (true));
  }

  @Test
  public void datasetAnalyses () throws Exception {
    assertThat (dataset1.analyses ().isEmpty (), is (true));
  }

  @Test
  public void datasetValues () throws Exception {
    assertThat (dataset1.values (), is (notNullValue ()));
  }

  @Test
  public void workspaceEmpty () throws Exception {
    assertThat (workspace.isEmpty (), is (true));
  }

  @Test
  public void workspacePut () throws Exception {
    assertThat (workspace.isEmpty (), is (true));
    assertThat (workspace.put ("mock", dataset1), is (nullValue ()));
    assertThat (workspace.size (), is (1));
  }

  @Test (expected = IllegalArgumentException.class)
  public void workspacePutWithWrongName () throws Exception {
    assertThat (workspace.isEmpty (), is (true));
    assertThat (workspace.put ("mock4", dataset1), is (nullValue ()));
    fail ();
  }

  @Test
  public void workspaceDoublePutSameName () throws Exception {
    assertThat (workspace.isEmpty (), is (true));
    assertThat (workspace.put ("mock", dataset1), is (nullValue ()));
    assertThat (workspace.size (), is (1));
    assertThat (workspace.put ("mock", dataset3), is ((Dataset<String, Double>) dataset1));
    assertThat (workspace.size (), is (1));
  }

  @Test
  public void workspaceDoublePutDifferentName () throws Exception {
    assertThat (workspace.isEmpty (), is (true));
    assertThat (workspace.put ("mock", dataset1), is (nullValue ()));
    assertThat (workspace.size (), is (1));
    assertThat (workspace.put ("mock2", dataset2), is (nullValue ()));
    assertThat (workspace.size (), is (2));
  }

  @Test
  public void workspaceDelete () throws Exception {
    assertThat (workspace.isEmpty (), is (true));
    assertThat (workspace.put ("mock", dataset1), is (nullValue ()));
    assertThat (workspace.size (), is (1));
    assertThat (workspace.remove ("mock"), is (notNullValue ()));
    assertThat (workspace.isEmpty (), is (true));
  }
}
