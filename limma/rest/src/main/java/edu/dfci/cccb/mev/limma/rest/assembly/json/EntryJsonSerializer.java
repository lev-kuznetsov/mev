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
package edu.dfci.cccb.mev.limma.rest.assembly.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.limma.domain.contract.Limma.Entry;

/**
 * @author levk
 * 
 */
public class EntryJsonSerializer extends JsonSerializer<Entry> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<Entry> handledType () {
    return Entry.class;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  public void serialize (Entry value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                      JsonProcessingException {
    jgen.writeStartObject ();
    jgen.writeStringField ("id", value.id ());
    jgen.writeNumberField ("logFoldChange", value.logFoldChange ());
    jgen.writeNumberField ("averageExpression", value.averageExpression ());
    jgen.writeNumberField ("pValue", value.pValue ());
    jgen.writeNumberField ("qValue", value.qValue ());
    jgen.writeEndObject ();
  }
}
