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
package edu.dfci.cccb.mev.hcl.rest.assembly.json;

import java.io.IOException;

import lombok.ToString;
import lombok.extern.log4j.Log4j;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.hcl.domain.contract.HclResult;

/**
 * @author levk
 * 
 */
@Log4j
@ToString
public class HclResultJsonSerializer extends JsonSerializer<HclResult> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<HclResult> handledType () {
    return HclResult.class;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  public void serialize (HclResult value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                          JsonProcessingException {
    if (log.isDebugEnabled ())
      log.debug ("Serializing " + value.getClass ());
    jgen.writeStartObject ();
    jgen.writeStringField ("name", value.name ());
    provider.defaultSerializeField ("dimension", value.dimension ().type (), jgen);
    provider.defaultSerializeField ("root", value.root (), jgen);
    jgen.writeEndObject ();
  }
}
