/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.rest.assembly.json.prototype;

import java.io.IOException;
import java.util.List;

import lombok.SneakyThrows;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selections;

/**
 * @author levk
 * 
 */
public abstract class AbstractDimensionJsonSerializer <T extends Dimension> extends JsonSerializer<T> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public abstract Class<T> handledType ();

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  public void serialize (T value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                  JsonProcessingException {
    jgen.writeStartObject ();
    serializeDimensionContent (value, jgen, provider);
    jgen.writeEndObject ();
  }

  protected void serializeDimensionContent (T value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                                     JsonProcessingException {
    provider.defaultSerializeField ("type", value.type (), jgen);
    serializeKeys ("keys", value.keys (), jgen, provider);
    serializeSelections ("selections", value.selections (), jgen, provider);
  }

  protected void serializeKeys (String field, List<String> keys, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                                                 JsonProcessingException {
    provider.defaultSerializeField (field, keys, jgen);
  }

  @SneakyThrows (SelectionNotFoundException.class)
  protected void serializeSelections (String field,
                                      Selections selections,
                                      JsonGenerator jgen,
                                      SerializerProvider provider) throws IOException,
                                                                  JsonProcessingException {
    jgen.writeArrayFieldStart (field);
    for (String selection : selections.list ())
      serializeSelection (selections.get (selection), jgen, provider);
    jgen.writeEndArray ();
  }

  protected void serializeSelection (Selection selection, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                                          JsonProcessingException {
    provider.defaultSerializeValue (selection, jgen);
  }
}
