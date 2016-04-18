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

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.ROW;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreException;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListAnalyses;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListSelections;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDataset;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;

/**
 * @author levk
 * 
 */
@Accessors (fluent = true, chain = true)
public class RserveDatasetDeserializer extends JsonDeserializer<Dataset> {

  private @Inject Provider<ValueStoreBuilder> valueStoreBuilder;

  /* (non-Javadoc)
   * @see
   * com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml
   * .jackson.core.JsonParser,
   * com.fasterxml.jackson.databind.DeserializationContext) */
  @Override
  @SneakyThrows (InvalidDatasetNameException.class)
  public Dataset deserialize (JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ValueStoreBuilder valueStoreBuilder = this.valueStoreBuilder.get ();
    final ArrayList<String> rowNames = new ArrayList<> ();
    final ArrayList<String> columnNames = new ArrayList<> ();
    int count = 0;
    ArrayNode a = jp.readValueAsTree ();
    for (JsonNode row : a) {
      JsonNode _row = row.get ("_row");
      String rowName = _row == null ? ("r" + count++) : _row.asText ();
      rowNames.add (rowName);
      for (Iterator<Entry<String, JsonNode>> fields = row.fields (); fields.hasNext ();) {
        Entry<String, JsonNode> field = fields.next ();
        if ("_row".equals (field.getKey ()))
          continue;
        try {
          valueStoreBuilder.add (field.getValue ().asDouble (), rowName, field.getKey ());
          if (!columnNames.contains (field.getKey ()))
            columnNames.add (field.getKey ());
        } catch (ValueStoreException e) {
          throw new IOException (e);
        }
      }
    }
    Map<String, Integer> rows = new LinkedHashMap<> ();
    for (int i = rowNames.size (); --i >= 0; rows.put (rowNames.get (i), i));
    Map<String, Integer> columns = new LinkedHashMap<>();
    for (int i = columnNames.size (); --i >= 0; columns.put (columnNames.get (i), i));
    return new SimpleDataset ("na", valueStoreBuilder.build (rows, columns), new ArrayListAnalyses (),
                              new SimpleDimension (ROW, rowNames, new ArrayListSelections (), null),
                              new SimpleDimension (COLUMN, columnNames, new ArrayListSelections (), null));
  }
}
