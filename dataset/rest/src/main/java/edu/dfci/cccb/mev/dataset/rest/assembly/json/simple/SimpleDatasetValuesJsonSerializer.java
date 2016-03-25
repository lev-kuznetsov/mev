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
package edu.dfci.cccb.mev.dataset.rest.assembly.json.simple;

import java.io.IOException;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Value;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;

/**
 * @author levk
 * 
 */
@ToString
@Log4j
public class SimpleDatasetValuesJsonSerializer extends JsonSerializer<Values> {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<Values> handledType () {
    return Values.class;
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  @SneakyThrows ({
                  InvalidDimensionTypeException.class,
                  InvalidCoordinateException.class,
                  DatasetException.class})
  public void serialize (Values values, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                        JsonProcessingException {
    if (log.isDebugEnabled ())
      log.debug ("Serializing DatasetValues of type " + values.getClass ().getSimpleName () + " to json");    
    writeValues (jgen, values);    
  }


  private void writeValue(JsonGenerator jgen, String row, String column, double value) throws JsonGenerationException, IOException{
	  jgen.writeStartObject ();
      jgen.writeStringField ("row", row);
      jgen.writeStringField ("column", column);
      jgen.writeNumberField ("value", value);
      jgen.writeEndObject ();
  }
  @SuppressWarnings ("unchecked")
  public void writeValues (JsonGenerator jgen, Values values) throws IOException,
                                                          JsonProcessingException,
                                                          DatasetException {   
    jgen.writeStartArray();
    
    if (values instanceof Iterable<?>) 
    	for (Value oValue : (Iterable<Value>) values) 	      
  	      writeValue(jgen, oValue.row (), oValue.column (), oValue.value ());                 
    else
    	throw new DatasetException(
    			String.format("%s class must implement Iterable in order to serialize itself", 
    					values.getClass ().getSimpleName ()));
    
    jgen.writeEndArray ();
  }
}
