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

package edu.dfci.cccb.mev.dataset.domain.support;

import static java.io.File.createTempFile;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.Values.Value;
import edu.dfci.cccb.mev.dataset.domain.support.binary.FlatFileStoreValuesAdapter;

public class FlatFileStoreValuesAdapterTest {

  @Test
  public void twoByTwo () throws Exception {
    File file = createTempFile ("mev.", ".dataset.test");
    try {
      try (RandomAccessFile writer = new RandomAccessFile (file, "rw")) {
        writer.writeDouble (.1);
        writer.writeDouble (.2);
        writer.writeDouble (.3);
        writer.writeDouble (.4);
      }

      LinkedHashMap<String, Map<String, Integer>> map = new LinkedHashMap<> ();
      Map<String, Integer> row = new HashMap<> ();
      row.put ("r1", 0);
      row.put ("r2", 1);
      Map<String, Integer> column = new HashMap<> ();
      column.put ("c1", 0);
      column.put ("c2", 1);
      map.put ("column", column);
      map.put ("row", row);

      try (FlatFileStoreValuesAdapter<String> values = new FlatFileStoreValuesAdapter<String> (file, map, true) {}) {

        Map<String, String> coordinate = new HashMap<> ();
        coordinate.put ("row", "r1");
        coordinate.put ("column", "c1");
        assertThat (values.get (asList (coordinate)).iterator ().next ().value (), is (.1));

        coordinate.clear ();
        coordinate.put ("row", "r1");
        coordinate.put ("column", "c2");
        assertThat (values.get (asList (coordinate)).iterator ().next ().value (), is (.2));

        coordinate.clear ();
        coordinate.put ("row", "r2");
        coordinate.put ("column", "c1");
        assertThat (values.get (asList (coordinate)).iterator ().next ().value (), is (.3));

        coordinate.clear ();
        coordinate.put ("row", "r2");
        coordinate.put ("column", "c2");
        assertThat (values.get (asList (coordinate)).iterator ().next ().value (), is (.4));
      }
    } finally {
      file.delete ();
    }
  }

  @Test
  public void threeByThreeByThree () throws Exception {
    File file = createTempFile ("mev.", ".dataset.test");
    try {
      Map<Map<String, String>, Double> wrote = new HashMap<> ();
      try (RandomAccessFile writer = new RandomAccessFile (file, "rw")) {
        for (int i = 0; i < 3; i++)
          for (int j = 10; j < 13; j++)
            for (int k = 110; k < 113; k++) {
              double v = ((double) (i * j * k)) / 10;
              Map<String, String> coordinate = new HashMap<> ();
              coordinate.put ("column", "c" + (k - 110));
              coordinate.put ("row", "r" + (j - 10));
              coordinate.put ("depth", "d" + i);
              wrote.put (coordinate, v);
              writer.writeDouble (v);
            }
      }

      LinkedHashMap<String, Map<String, Integer>> map = new LinkedHashMap<> ();
      Map<String, Integer> row = new HashMap<> ();
      row.put ("r0", 0);
      row.put ("r1", 1);
      row.put ("r2", 2);
      Map<String, Integer> column = new HashMap<> ();
      column.put ("c0", 0);
      column.put ("c1", 1);
      column.put ("c2", 2);
      Map<String, Integer> depth = new HashMap<> ();
      depth.put ("d0", 0);
      depth.put ("d1", 1);
      depth.put ("d2", 2);
      map.put ("column", column);
      map.put ("row", row);
      map.put ("depth", depth);

      try (FlatFileStoreValuesAdapter<String> values = new FlatFileStoreValuesAdapter<String> (file, map, true) {}) {

        List<Map<String, String>> coordinates = new ArrayList<> ();
        for (int i = 0; i < 3; i++)
          for (int j = 0; j < 3; j++)
            for (int k = 0; k < 3; k++) {
              Map<String, String> coordinate = new HashMap<> ();
              coordinate.put ("row", "r" + i);
              coordinate.put ("column", "c" + j);
              coordinate.put ("depth", "d" + k);
              coordinates.add (coordinate);
            }

        for (Value<String, Double> value : values.get (coordinates)) {
          assertThat (value.value (), is (wrote.get (value.coordinates ())));
        }
      }
    } finally {
      file.delete ();
    }
  }
}
