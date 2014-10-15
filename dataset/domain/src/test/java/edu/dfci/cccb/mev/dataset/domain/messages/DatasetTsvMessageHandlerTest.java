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

package edu.dfci.cccb.mev.dataset.domain.messages;

import static com.google.common.collect.ImmutableMap.of;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.TEXT_TSV_TYPE;
import static java.lang.Double.NaN;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Provides;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.Values;
import edu.dfci.cccb.mev.dataset.domain.Values.Value;
import edu.dfci.cccb.mev.dataset.domain.annotation.NameOf;
import edu.dfci.cccb.mev.dataset.domain.guice.DatasetModule;

@RunWith (JukitoRunner.class)
public class DatasetTsvMessageHandlerTest {

  private @Inject @Named (DatasetTsvMessageHandler.ROW) String ROW;
  private @Inject @Named (DatasetTsvMessageHandler.COLUMN) String COLUMN;
  private @Inject DatasetTsvMessageHandler handler;

  public static class DatasetTsvHandlerTestingModule extends JukitoModule {
    @Provides
    @NameOf (Dataset.class)
    @Singleton
    public String datasetName () {
      return "mock";
    }

    protected void configureTest () {
      install (new DatasetModule ());
    }
  }

  @Test
  public void read () throws Exception {
    Dataset<String, Double> ds = handler.readFrom (null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   new ByteArrayInputStream (("  \tc1\tc2\tc3\n" +
                                                                              "r1\t.1\t.2\t.4\n" +
                                                                              "r2\t.4\t.5\t.6").getBytes ()));
    assertThat (ds.analyses ().entrySet ().size (), is (0));
    assertThat (ds.dimensions ().get ("column").size (), is (3));
    assertThat (ds.dimensions ().get ("column").get (2), is ("c3"));
    assertThat (ds.dimensions ().get ("row").size (), is (2));
    assertThat (ds.dimensions ().get ("row").get (1), is ("r2"));
    Iterator<Value<String, Double>> v = ds.values ()
                                          .get (asList ((Map<String, String>) of ("column", "c1", "row", "r1"),
                                                        of ("column", "c3", "row", "r1")))
                                          .iterator ();
    assertThat (v.next ().value (), is (.1));
    assertThat (v.next ().value (), is (.4));
    assertFalse (v.hasNext ());
  }

  @Test
  public void readSpecial () throws Exception {
    Dataset<String, Double> ds = handler.readFrom (null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   new ByteArrayInputStream (("# comment\n" +
                                                                              "  \tc1\tc2\tc3\n" +
                                                                              "r1\tnull\tNA\tNaN\n" +
                                                                              "# comment\n" +
                                                                              "r2\t-Inf\tInfinity\tInf").getBytes ()));
    Iterator<Value<String, Double>> v = ds.values ()
                                          .get (asList ((Map<String, String>) of ("column", "c1", "row", "r1"),
                                                        of ("column", "c3", "row", "r1")))
                                          .iterator ();
    assertThat (v.next ().value (), is (NaN));
    assertThat (v.next ().value (), is (NaN));
    assertFalse (v.hasNext ());
  }

  @Test
  public void isReadable () throws Exception {
    assertTrue (handler.isReadable (Dataset.class, null, null, TEXT_TSV_TYPE));
  }

  @Test
  @SuppressWarnings ("unchecked")
  public void write () throws Exception {
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

    handler.writeTo (ds, null, null, null, null, null, actual);

    assertThat (actual.toString (), startsWith ("\tc1\tc2\tc3\n" +
                                                "r1\t0.1\t0.2\t0.3\n" +
                                                "r2\t0.4\t0.5\t0.6"));
  }

  @Test
  public void isWriteable () throws Exception {
    assertTrue (handler.isWriteable (Dataset.class, null, null, TEXT_TSV_TYPE));
  }
}
