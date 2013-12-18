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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.dataset.rest.assembly.json.prototype.AbstractDimensionJsonSerializer;
import edu.dfci.cccb.mev.hcl.domain.simple.SimpleHierarchicallyClusteredDimension;

/**
 * @author levk
 * 
 */
@ToString
public class SimpleHierarchicallyClusteredDimensionJsonSerializer extends AbstractDimensionJsonSerializer<SimpleHierarchicallyClusteredDimension> {

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.assembly.json.prototype.
   * AbstractDimensionJsonSerializer#handledType() */
  @Override
  public Class<SimpleHierarchicallyClusteredDimension> handledType () {
    return SimpleHierarchicallyClusteredDimension.class;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.assembly.json.prototype.
   * AbstractDimensionJsonSerializer
   * #serializeDimensionContent(edu.dfci.cccb.mev.
   * dataset.domain.contract.Dimension,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider) */
  @Override
  protected void serializeDimensionContent (SimpleHierarchicallyClusteredDimension value,
                                            JsonGenerator jgen,
                                            SerializerProvider provider) throws IOException, JsonProcessingException {
    super.serializeDimensionContent (value, jgen, provider);
    provider.defaultSerializeField ("root", value.root (), jgen);
  }
}
