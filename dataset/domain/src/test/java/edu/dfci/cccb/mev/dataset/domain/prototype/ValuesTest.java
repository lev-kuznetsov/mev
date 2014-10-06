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

import static com.google.common.collect.ImmutableMap.of;
import static edu.dfci.cccb.mev.dataset.domain.prototype.ValuesAdapter.all;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.Dimension;

/**
 * @author levk
 */
public class ValuesTest {

  private static Dimension<String> dimension (final String name, final String... keys) {
    return new Dimension<String> () {

      @Override
      public String name () {
        return name;
      }

      @Override
      public Iterator<String> iterator () {
        return Arrays.asList (keys).iterator ();
      }

      @Override
      public int size () {
        return keys.length;
      }

      @Override
      public String get (int index) {
        return keys[index];
      }

      @Override
      public String toString () {
        return Arrays.toString (keys);
      }
    };
  }

  @SuppressWarnings ("serial")
  @Test
  public void allWalker () throws Throwable {
    Iterator<Map<String, String>> actual = all (Arrays.asList (dimension ("row", "r1", "r2", "r3"),
                                                               dimension ("column", "c1", "c2", "c3"),
                                                               dimension ("depth", "d1", "d2", "d3"))).iterator ();
    Iterator<Map<String, String>> expected = new ArrayList<Map<String, String>> () {
      {
        for (int row = 1; row <= 3; row++)
          for (int column = 1; column <= 3; column++)
            for (int depth = 1; depth <= 3; depth++)
              add (of ("row", "r" + row, "column", "c" + column, "depth", "d" + depth));
      }
    }.iterator ();
    while (actual.hasNext () && expected.hasNext ())
      assertThat (actual.next (), is (expected.next ()));
    assertTrue (actual.hasNext () == expected.hasNext ());
  }

  @SuppressWarnings ("serial")
  @Test
  public void allWalkerVarArg () throws Throwable {
    Iterator<Map<String, String>> actual = all (dimension ("row", "r1", "r2", "r3"),
                                                dimension ("column", "c1", "c2", "c3"),
                                                dimension ("depth", "d1", "d2", "d3")).iterator ();
    Iterator<Map<String, String>> expected = new ArrayList<Map<String, String>> () {
      {
        for (int row = 1; row <= 3; row++)
          for (int column = 1; column <= 3; column++)
            for (int depth = 1; depth <= 3; depth++)
              add (of ("row", "r" + row, "column", "c" + column, "depth", "d" + depth));
      }
    }.iterator ();
    while (actual.hasNext () && expected.hasNext ())
      assertThat (actual.next (), is (expected.next ()));
    assertTrue (actual.hasNext () == expected.hasNext ());
  }
}
