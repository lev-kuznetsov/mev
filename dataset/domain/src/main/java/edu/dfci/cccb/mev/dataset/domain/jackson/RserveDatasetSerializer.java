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

package edu.dfci.cccb.mev.dataset.domain.jackson;

import static com.google.common.collect.ImmutableMap.of;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import lombok.SneakyThrows;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.InvalidCoordinateSetException;
import edu.dfci.cccb.mev.dataset.domain.Values.Value;

/**
 * @author levk
 * @since CRYSTAL
 */
public class RserveDatasetSerializer <K, V> extends JsonSerializer<Dataset<K, V>> {

  @SuppressWarnings ("unchecked")
  private Class<Dataset<K, V>> cast (Class<?> t) {
    return (Class<Dataset<K, V>>) t;
  }

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<Dataset<K, V>> handledType () {
    return cast (Dataset.class);
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  @SneakyThrows (InvalidCoordinateSetException.class)
  public void serialize (Dataset<K, V> dataset, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                                JsonProcessingException {
    final Dimension<K> rows = dataset.dimensions ().get ("row");
    final Dimension<K> columns = dataset.dimensions ().get ("column");
    Iterable<Map<String, K>> query = new Iterable<Map<String, K>> () {
      @Override
      public Iterator<Map<String, K>> iterator () {
        return new Iterator<Map<String, K>> () {
          private Iterator<K> r = rows.iterator ();
          private K row = r.next ();
          private Iterator<K> c = columns.iterator ();

          @Override
          public boolean hasNext () {
            return r.hasNext () || c.hasNext ();
          }

          @Override
          public Map<String, K> next () {
            if (!c.hasNext ()) {
              c = columns.iterator ();
              row = r.next ();
            }
            return of ("row", row, "column", c.next ());
          }

          @Override
          public void remove () {
            throw new UnsupportedOperationException ();
          }
        };
      }
    };
    Iterator<Value<K, V>> values = dataset.values ().get (query).iterator ();

    jgen.writeStartArray ();
    for (K row : rows) {
      jgen.writeStartObject ();
      for (K column : columns) {
        Value<K, V> v = values.next ();
        jgen.writeObjectField (column.toString (), v.value ());
        // provider.defaultSerializeField (column.toString (), v.value (),
        // jgen);
      }
      // provider.defaultSerializeField ("_row", row, jgen);
      jgen.writeObjectField ("_row", row);
      jgen.writeEndObject ();
    }
    jgen.writeEndArray ();
    jgen.flush ();
  }
}
