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
import static java.lang.Double.NaN;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Values.Value;

public class DatasetTsvDeserializerTest {

  private DatasetTsvDeserializer d;

  @Before
  public void setUpDeserializer () {
    d = new DatasetTsvDeserializer ();
  }

  @Test
  public void deserialize () throws Exception {
    Dataset<String, Double> ds = d.deserialize ("test", new ByteArrayInputStream (("  \tc1\tc2\tc3\n" +
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
  public void deserializeSpecial () throws Exception {
    Dataset<String, Double> ds = d.deserialize ("test",
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
}
