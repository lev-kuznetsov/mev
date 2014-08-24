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

package edu.dfci.cccb.mev.dataset.domain.guice;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Values.Value;
import edu.dfci.cccb.mev.dataset.domain.support.Builder;

public class DatasetModuleTest {

  @Test
  public void builder () throws Exception {
    assertThat (Guice.createInjector (new DatasetModule ())
                     .getInstance (Key.get (new TypeLiteral<Builder<String, Double>> () {})),
                is (instanceOf (Builder.class)));
  }

  @Test
  public void buildDataset () throws Exception {
    Dataset<String, Double> dataset = Guice.createInjector (new DatasetModule ())
                                           .getInstance (Key.get (new TypeLiteral<Builder<String, Double>> () {}))
                                           .build ("mock",
                                                   new ByteArrayInputStream (("..\tc0\tc1\tc2\tc3\n" +
                                                                              "r0\t.0\t.1\t.2\t.3\n" +
                                                                              "r1\t.1\t.2\t.3\t.4\n" +
                                                                              "r2\t.2\t.3\t.4\t.5").getBytes ()));
    assertThat (dataset.name (), is ("mock"));
    assertThat (dataset.dimensions ().size (), is (2));
    Iterator<String> expected = asList ("c0", "c1", "c2", "c3").iterator ();
    Iterator<String> actual = dataset.dimensions ().get ("column").iterator ();
    for (; expected.hasNext () || actual.hasNext (); assertThat (expected.next (), is (actual.next ())));
    expected = asList ("r0", "r1", "r2").iterator ();
    actual = dataset.dimensions ().get ("row").iterator ();
    for (; expected.hasNext () || actual.hasNext (); assertThat (expected.next (), is (actual.next ())));
    Iterator<Value<String, Double>> value = dataset.values ()
                                                   .get (asList ((Map<String, String>) of ("column", "c2",
                                                                                           "row", "r2")))
                                                   .iterator ();
    assertTrue (value.hasNext ());
    assertThat (value.next ().value (), closeTo (.4, .001));
  }
}
