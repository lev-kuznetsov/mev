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

import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.join;
import static com.google.common.collect.ImmutableSet.of;
import static edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvDeserializer.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvDeserializer.ROW;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.InvalidCoordinateSetException;

/**
 * @author levk
 * @since ASHA
 */
public class DatasetTsvSerializer {

  @SneakyThrows (InvalidCoordinateSetException.class)
  public <K> void serialize (final Dataset<K, ?> dataset, OutputStream stream) {
    PrintStream out = new PrintStream (stream);

    out.println ("\t" + join (dataset.dimensions ().get (COLUMN), "\t"));
    for (final K row : dataset.dimensions ().get (ROW))
      out.println (row + "\t" + join (forEach (dataset.values ().get (new AbstractList<Map<String, K>> () {
        private final Dimension<K> columns = dataset.dimensions ().get (COLUMN);

        @Override
        public Map<String, K> get (final int index) {
          return new AbstractMap<String, K> () {
            private final K column = columns.get (index);

            @Override
            public Set<Entry<String, K>> entrySet () {
              return of (entry (COLUMN, column), entry (ROW, row));
            }

            private Entry<String, K> entry (final String key, final K value) {
              return new Entry<String, K> () {

                @Override
                public String getKey () {
                  return key;
                }

                @Override
                public K getValue () {
                  return value;
                }

                @Override
                public K setValue (K value) {
                  throw new UnsupportedOperationException ();
                }
              };
            }
          };
        }

        @Override
        public int size () {
          return columns.size ();
        }
      })).value (), "\t"));

    out.flush ();
  }
}
