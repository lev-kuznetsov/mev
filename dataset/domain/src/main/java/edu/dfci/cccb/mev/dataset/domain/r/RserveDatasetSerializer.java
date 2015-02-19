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
package edu.dfci.cccb.mev.dataset.domain.r;

import java.io.IOException;

import lombok.SneakyThrows;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;

/**
 * @author levk
 * 
 */
public class RserveDatasetSerializer extends JsonSerializer<Dataset> {

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  @SneakyThrows ({ InvalidDimensionTypeException.class, InvalidCoordinateException.class })
  public void serialize (Dataset value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                        JsonProcessingException {
    jgen.writeStartArray ();
    for (String row : value.dimension (Type.ROW).keys ()) {
      jgen.writeStartObject ();
      for (String column : value.dimension (Type.COLUMN).keys ()) {
        jgen.writeNumberField (column, value.values ().get (row, column));
      }
      jgen.writeStringField ("_row", row);
      jgen.writeEndObject ();
    }
    jgen.writeEndArray ();
  }
}
