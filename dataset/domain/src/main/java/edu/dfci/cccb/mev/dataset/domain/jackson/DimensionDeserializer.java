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

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;

/**
 * @author levk
 * @since CRYSTAL
 */
public class DimensionDeserializer <K, V> extends JsonDeserializer<Dimension<K>> {

  private @Inject Provider<Dataset<K, V>> dataset;

  @SuppressWarnings ("unchecked")
  private <T extends Dimension<K>> Class<T> cast (Class<?> type) {
    return (Class<T>) type;
  }

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonDeserializer#handledType() */
  @Override
  public Class<?> handledType () {
    return cast (Dimension.class);
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml
   * .jackson.core.JsonParser,
   * com.fasterxml.jackson.databind.DeserializationContext) */
  @Override
  public Dimension<K> deserialize (JsonParser p, DeserializationContext ctxt) throws IOException,
                                                                             JsonProcessingException {
    return dataset.get ().dimensions ().get (((JsonNode) p.readValueAsTree ().get ("name")).asText ());
  }
}
