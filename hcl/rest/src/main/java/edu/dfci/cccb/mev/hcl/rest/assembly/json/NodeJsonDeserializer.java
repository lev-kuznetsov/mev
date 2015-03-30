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

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import edu.dfci.cccb.mev.hcl.domain.contract.Node;
import edu.dfci.cccb.mev.hcl.domain.mock.MockBranch;
import edu.dfci.cccb.mev.hcl.domain.mock.MockLeaf;

/**
 * @author levk
 * 
 */
public class NodeJsonDeserializer extends JsonDeserializer<Node> {

  private Node construct (JsonNode tree) {
    JsonNode name = tree.get ("name");
    return name == null
                       ? new MockBranch (tree.get ("distance").asDouble (),
                                         new TreeSet<Node> (asList (construct (tree.get ("left")),
                                                                    construct (tree.get ("right")))))
                       : new MockLeaf (name.asText ());
  }

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml
   * .jackson.core.JsonParser,
   * com.fasterxml.jackson.databind.DeserializationContext) */
  @Override
  public Node deserialize (JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    return construct ((JsonNode) jp.readValueAsTree ());
  }
}
