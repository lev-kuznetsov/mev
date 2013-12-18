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
package edu.dfci.cccb.mev.web.rest.assembly.json;

import static java.util.Calendar.AM_PM;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.LONG;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static java.util.Locale.US;

import java.io.IOException;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author levk
 * 
 */
public class CalendarJsonSerializer extends JsonSerializer<Calendar> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<Calendar> handledType () {
    return Calendar.class;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  public void serialize (Calendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                         JsonProcessingException {
    jgen.writeStartObject ();
    jgen.writeNumberField ("timeInMillis", value.getTimeInMillis ());
    jgen.writeNumberField ("seconds", value.get (SECOND));
    jgen.writeNumberField ("minutes", value.get (MINUTE));
    jgen.writeNumberField ("hours", value.get (HOUR));
    jgen.writeStringField ("period", value.getDisplayName (AM_PM, LONG, US));
    jgen.writeEndObject ();
  }
}
