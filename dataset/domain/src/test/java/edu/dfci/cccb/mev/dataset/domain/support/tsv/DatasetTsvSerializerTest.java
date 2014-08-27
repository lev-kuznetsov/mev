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

package edu.dfci.cccb.mev.dataset.domain.support.tsv;

import static com.google.common.collect.ImmutableMap.of;
import static edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvDeserializer.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvDeserializer.ROW;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.Values;
import edu.dfci.cccb.mev.dataset.domain.Values.Value;

public class DatasetTsvSerializerTest {

  private DatasetTsvSerializer s;

  @Before
  public void setUpSerializer () {
    s = new DatasetTsvSerializer ();
  }

  @Test
  @SuppressWarnings ("unchecked")
  public void serialize () throws Exception {
    Dimension<String> column = mock (Dimension.class);
    when (column.iterator ()).thenReturn (asList ("c1", "c2", "c3").iterator ());
    when (column.size ()).thenReturn (3);

    Dimension<String> row = mock (Dimension.class);
    when (row.iterator ()).thenReturn (asList ("r1", "r2").iterator ());
    when (row.size ()).thenReturn (2);

    Dataset<String, Double> ds = mock (Dataset.class);
    Map<String, Dimension<String>> dimensions = mock (Map.class);
    when (dimensions.get (COLUMN)).thenReturn (column);
    when (dimensions.get (ROW)).thenReturn (row);
    when (ds.dimensions ()).thenReturn (dimensions);
    Values<String, Double> values = mock (Values.class);
    when (values.get (any (Iterable.class))).thenReturn (asList (new Value<String, Double> (.1,
                                                                                            of (ROW, "r1", COLUMN, "c1")),
                                                                 new Value<String, Double> (.2,
                                                                                            of (ROW, "r1", COLUMN, "c2")),
                                                                 new Value<String, Double> (.3,
                                                                                            of (ROW, "r1", COLUMN, "c3"))))
                                            .thenReturn (asList (new Value<String, Double> (.4,
                                                                                            of (ROW, "r2", COLUMN, "c1")),
                                                                 new Value<String, Double> (.5,
                                                                                            of (ROW, "r2", COLUMN, "c2")),
                                                                 new Value<String, Double> (.6,
                                                                                            of (ROW, "r2", COLUMN, "c3"))));
    when (ds.values ()).thenReturn (values);

    ByteArrayOutputStream actual = new ByteArrayOutputStream ();
    s.serialize (ds, actual);

    assertThat (actual.toString (), startsWith ("\tc1\tc2\tc3\n" +
                                                "r1\t0.1\t0.2\t0.3\n" +
                                                "r2\t0.4\t0.5\t0.6"));
  }
}
