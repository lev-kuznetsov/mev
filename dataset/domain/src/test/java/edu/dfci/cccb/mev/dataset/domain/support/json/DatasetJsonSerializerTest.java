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

package edu.dfci.cccb.mev.dataset.domain.support.json;

import static com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance;
import static com.google.inject.Guice.createInjector;
import static com.mycila.inject.internal.guava.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonIntrospectorBinder;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;
import edu.dfci.cccb.mev.dataset.domain.Analysis;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.Values;
import edu.dfci.cccb.mev.dataset.domain.Values.Value;

public class DatasetJsonSerializerTest {

  @SuppressWarnings ("unchecked")
  @Test
  public void serialize () throws Exception {
    Dataset<String, Double> dataset = mock (Dataset.class);
    when (dataset.name ()).thenReturn ("mock");
    Dimension<String> row = mock (Dimension.class);
    when (row.name ()).thenReturn ("row");
    when (row.iterator ()).thenReturn (asList ("r1", "r2", "r3").iterator ());
    Dimension<String> column = mock (Dimension.class);
    when (column.name ()).thenReturn ("column");
    when (column.iterator ()).thenReturn (asList ("c1", "c2", "c3", "c4").iterator ());
    Map<String, Dimension<String>> dimensions = new LinkedHashMap<> ();
    dimensions.put ("row", row);
    dimensions.put ("column", column);
    when (dataset.dimensions ()).thenReturn (dimensions);
    when (dataset.analyses ()).thenReturn (new HashMap<String, Analysis> ());
    Values<String, Double> values = mock (Values.class);
    List<Value<String, Double>> vals = asList (new Value<String, Double> (.0, of ("row", "r1", "column", "c1")),
                                               new Value<String, Double> (.1, of ("row", "r1", "column", "c2")),
                                               new Value<String, Double> (.2, of ("row", "r1", "column", "c3")),
                                               new Value<String, Double> (.3, of ("row", "r1", "column", "c4")),
                                               new Value<String, Double> (-.1, of ("row", "r2", "column", "c1")),
                                               new Value<String, Double> (-.2, of ("row", "r2", "column", "c2")),
                                               new Value<String, Double> (-.3, of ("row", "r2", "column", "c3")),
                                               new Value<String, Double> (-.4, of ("row", "r2", "column", "c4")),
                                               new Value<String, Double> (.11, of ("row", "r3", "column", "c1")),
                                               new Value<String, Double> (-.22, of ("row", "r3", "column", "c2")),
                                               new Value<String, Double> (.33, of ("row", "r3", "column", "c3")),
                                               new Value<String, Double> (-.44, of ("row", "r3", "column", "c4")));
    when (values.get ((Iterable<Map<String, String>>) anyObject ())).thenReturn (vals);
    when (dataset.values ()).thenReturn (values);

    assertEquals (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.with (DatasetJsonSerializer.class);
      }

      public void configure (JacksonIntrospectorBinder binder) {
        binder.useInstance (new JaxbAnnotationIntrospector (defaultInstance ()));
      }
    }).getInstance (ObjectMapper.class).writeValueAsString (dataset),
                  "{\"name\":\"mock\",\"dimensions\":[{\"name\":\"row\",\"keys\":[\"r"
                          + "1\",\"r2\",\"r3\"]},{\"name\":\"column\",\"keys\":[\"c1\",\"c2\""
                          + ",\"c3\",\"c4\"]}],\"values\":[{\"coordinates\":{\"column\":\"c1\""
                          + ",\"row\":\"r1\"},\"value\":0.0},{\"coordinates\":{\"column\":\"c"
                          + "2\",\"row\":\"r1\"},\"value\":0.1},{\"coordinates\":{\"column\":"
                          + "\"c3\",\"row\":\"r1\"},\"value\":0.2},{\"coordinates\":{\"column"
                          + "\":\"c4\",\"row\":\"r1\"},\"value\":0.3},{\"coordinates\":{\"col"
                          + "umn\":\"c1\",\"row\":\"r2\"},\"value\":-0.1},{\"coordinates\":{\""
                          + "column\":\"c2\",\"row\":\"r2\"},\"value\":-0.2},{\"coordinates\""
                          + ":{\"column\":\"c3\",\"row\":\"r2\"},\"value\":-0.3},{\"coordinat"
                          + "es\":{\"column\":\"c4\",\"row\":\"r2\"},\"value\":-0.4},{\"coord"
                          + "inates\":{\"column\":\"c1\",\"row\":\"r3\"},\"value\":0.11},{\"c"
                          + "oordinates\":{\"column\":\"c2\",\"row\":\"r3\"},\"value\":-0.22}"
                          + ",{\"coordinates\":{\"column\":\"c3\",\"row\":\"r3\"},\"value\":0"
                          + ".33},{\"coordinates\":{\"column\":\"c4\",\"row\":\"r3\"},\"value"
                          + "\":-0.44}],\"analyses\":[]}",
                  false);
  }
}
