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

package edu.dfci.cccb.mev.dataset.domain.support.binary;

import static ch.lambdaj.Lambda.max;
import static java.lang.Math.min;
import static java.nio.channels.FileChannel.MapMode.READ_ONLY;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.dfci.cccb.mev.dataset.domain.InvalidCoordinateSetException;
import edu.dfci.cccb.mev.dataset.domain.Values;

/**
 * Flat file based value store
 * 
 * @author levk
 * @since BAYLIE
 */
public class FlatFileStoreValuesAdapter <K> implements Values<K, Double>, AutoCloseable {

  private static final long BUFFER_SIZE = 1 << 30;

  private final boolean cleanup;
  private final File file;
  private final List<MappedByteBuffer> buffers = new ArrayList<> ();
  private LinkedHashMap<String, Map<K, Integer>> map;

  public FlatFileStoreValuesAdapter (File file, LinkedHashMap<String, Map<K, Integer>> map, boolean cleanup) throws FileNotFoundException,
                                                                                                            IOException {
    try (RandomAccessFile access = new RandomAccessFile (file, "r")) {
      long size = 8L;
      for (Map<K, Integer> dimension : map.values ())
        size *= 1 + (Integer) max (dimension.values ());
      for (long offset = 0; offset < size; offset += BUFFER_SIZE)
        buffers.add (access.getChannel ().map (READ_ONLY, offset, min (size - offset, BUFFER_SIZE)));
    }
    this.file = file;
    this.map = map;
    this.cleanup = cleanup;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Values#get(java.lang.Iterable) */
  @Override
  public Iterable<Value<K, Double>> get (final Iterable<Map<String, K>> coordinates) throws InvalidCoordinateSetException {
    return new Iterable<Value<K, Double>> () {
      @Override
      public Iterator<Value<K, Double>> iterator () {
        return new Iterator<Value<K, Double>> () {
          private final Iterator<Map<String, K>> iterator = coordinates.iterator ();

          @Override
          public boolean hasNext () {
            return iterator.hasNext ();
          }

          @Override
          public Value<K, Double> next () {
            Map<String, K> coordinate = iterator.next ();
            long position = position (coordinate, map);
            return new Value<> (buffers.get ((int) (position / BUFFER_SIZE))
                                       .getDouble ((int) (position % BUFFER_SIZE)),
                                coordinate);
          }

          @Override
          public void remove () {
            throw new UnsupportedOperationException ();
          }
        };
      }
    };
  }

  private static <K> long position (Map<String, K> coordinate, LinkedHashMap<String, Map<K, Integer>> map) {
    long position = 0L;
    long space = 1;
    for (Entry<String, Map<K, Integer>> entry : map.entrySet ()) {
      Map<K, Integer> dimension = entry.getValue ();
      position += space * dimension.get (coordinate.get (entry.getKey ()));
      space *= dimension.size ();
    }
    return position * 8;
  }

  /* (non-Javadoc)
   * @see java.lang.AutoCloseable#close() */
  @Override
  public void close () throws Exception {
    buffers.clear ();
    if (cleanup)
      file.delete ();
  }
}
