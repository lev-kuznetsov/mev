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

package edu.dfci.cccb.mev.googleplus.domain.support.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.api.services.plus.model.Person.Organizations;

/**
 * @author levk
 * @since CRYSTAL
 */
public class OrganizationsSerializer extends JsonSerializer<Organizations> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<Organizations> handledType () {
    return Organizations.class;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  public void serialize (Organizations value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                              JsonProcessingException {
    jgen.writeStartObject ();
    jgen.writeStringField ("department", value.getDepartment ());
    jgen.writeStringField ("description", value.getDescription ());
    jgen.writeStringField ("end-date", value.getEndDate ());
    jgen.writeStringField ("location", value.getLocation ());
    jgen.writeStringField ("name", value.getName ());
    jgen.writeStringField ("start-date", value.getStartDate ());
    jgen.writeStringField ("title", value.getTitle ());
    jgen.writeStringField ("type", value.getType ());
    jgen.writeBooleanField ("primary", value.getPrimary ());
    jgen.writeEndObject ();
  }
}
