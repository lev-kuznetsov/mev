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

package edu.dfci.cccb.mev.dataset.domain.support.json;

import static edu.dfci.cccb.mev.dataset.domain.prototype.ValuesAdapter.all;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateSetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values.Value;

/**
 * Dataset JSON serializer
 * 
 * @author levk
 * @since BAYLIE
 */
public class DatasetJsonSerializer extends JsonSerializer<Dataset<?, ?>> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<Dataset<?, ?>> handledType () {
    return cast (Dataset.class);
  }

  @SuppressWarnings ("unchecked")
  private static <T> Class<T> cast (Class<?> clazz) {
    return (Class<T>) clazz;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  public void serialize (final Dataset<?, ?> dataset, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                                      JsonProcessingException {
    try {
      jgen.writeStartObject ();

      jgen.writeStringField ("name", dataset.name ());

      provider.defaultSerializeField ("dimensions", dataset.dimensions ().values (), jgen);

      provider.defaultSerializeField ("values", values (dataset), jgen);

      provider.defaultSerializeField ("analyses", dataset.analyses ().values (), jgen);

      jgen.writeEndObject ();
    } catch (InvalidCoordinateSetException e) {
      throw new IOException (e);
    }
  }

  private static <K, V> Iterable<Value<K, V>> values (Dataset<K, V> dataset) throws InvalidCoordinateSetException {
    return dataset.values ().get (all (dataset.dimensions ().values ()));
  }
}
