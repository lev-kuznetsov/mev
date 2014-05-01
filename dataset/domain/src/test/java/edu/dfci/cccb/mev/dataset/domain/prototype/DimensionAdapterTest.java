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

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Delegate;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;

public class DimensionAdapterTest {

  private Dimension<String> rows1;
  private Dimension<String> columns;
  private Dimension<String> rows2;

  private Map<String, Dimension<String>> dimensions;

  @Before
  public void setUp () throws Exception {
    rows1 = new DimensionAdapter<String> ("row") {
      private final List<String> keys = asList ("r1", "r2", "r3");

      public String get (int index) {
        return keys.get (index);
      }

      public int size () {
        return keys.size ();
      }

      public Iterator<String> iterator () {
        return keys.iterator ();
      }
    };
    rows2 = new DimensionAdapter<String> ("row") {
      private final @Delegate List<String> keys = asList ("r2", "r2", "r1");
    };
    columns = new DimensionAdapter<String> ("column") {
      private final @Delegate List<String> keys = asList ("c1", "c2", "c3");
    };

    dimensions = DimensionAdapter.dimensions (rows1, columns);
  }

  @Test
  public void dimensionName () throws Exception {
    assertThat (rows1.name (), is ("row"));
  }

  @Test
  public void dimensionKeys () throws Exception {
    Iterator<String> actual = rows1.iterator ();
    Iterator<String> expected = asList ("r1", "r2", "r3").iterator ();
    while (actual.hasNext () && expected.hasNext ())
      assertThat (actual.next (), is (expected.next ()));
    assertTrue (actual.hasNext () == expected.hasNext ());
  }

  @Test
  public void dimensionSize () throws Exception {
    assertThat (rows1.size (), is (3));
  }

  @Test
  public void dimensionGet () throws Exception {
    assertThat (columns.get (0), is ("c1"));
    assertThat (columns.get (1), is ("c2"));
    assertThat (columns.get (2), is ("c3"));
  }

  @Test
  public void dimensionsCreate () throws Exception {
    assertThat (dimensions.size (), is (2));
  }

  @Test
  public void dimensionsPut () throws Exception {
    assertThat (dimensions.put ("row", rows2), equalTo (rows1));
  }

  @SuppressWarnings ("unchecked")
  @Test (expected = IllegalArgumentException.class)
  public void dimensionsPutNonExistantDimensionName () throws Exception {
    Dimension<String> depth = mock (Dimension.class);
    when (depth.name ()).thenReturn ("depth");
    dimensions.put ("depth", depth);
    fail ();
  }

  @SuppressWarnings ("unchecked")
  @Test (expected = IllegalArgumentException.class)
  public void dimensionsPutNonMatchingDimensionName () throws Exception {
    Dimension<String> dimension = mock (Dimension.class);
    when (dimension.name ()).thenReturn ("column");
    dimensions.put ("row", dimension);
    fail ();
  }

  @Test
  public void dimensionsDoublePut () throws Exception {
    assertThat (dimensions.put ("row", rows2), is (notNullValue ()));
  }

  @Test
  public void dimensionEquals () throws Exception {
    assertTrue (rows1.equals (rows1));
    assertFalse (rows1.equals (rows2));
  }

  @Test
  public void dimensionHashCode () throws Exception {
    assertThat (rows1.hashCode (), is (rows1.hashCode ()));
  }

  @Test
  public void dimensionIterator () throws Exception {
    Iterator<String> actual = rows1.iterator (), expected = asList ("r1", "r2", "r3").iterator ();
    for (; actual.hasNext () && expected.hasNext ();)
      assertThat (actual.next (), is (expected.next ()));
    assertThat (actual.hasNext () || expected.hasNext (), is (false));
  }

  @Test (expected = Exception.class)
  public void dimensionIteratorRemove () throws Exception {
    rows1.iterator ().remove ();
    fail ();
  }
}
