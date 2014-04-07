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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonIntrospectorBinder;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;
import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateSetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;

public class DatasetJsonSerializerTest {

  @Test
  public void simple () throws Exception {
    assertThat (createInjector (new JacksonModule () {
      public void configure (JacksonSerializerBinder binder) {
        binder.with (DatasetJsonSerializer.class);
      }

      public void configure (JacksonIntrospectorBinder binder) {
        binder.useInstance (new JaxbAnnotationIntrospector (defaultInstance ()));
      }
    }).getInstance (ObjectMapper.class)
      .writeValueAsString (new Dataset<String, Double> () {

        @Override
        public String name () {
          return "mock";
        }

        @Override
        public Map<String, Dimension<String>> dimensions () {
          return new LinkedHashMap<String, Dimension<String>> () {
            private static final long serialVersionUID = 1L;

            {
              put ("row", new Dimension<String> () {
                public int size () {
                  return 3;
                }

                public String name () {
                  return "row";
                }

                @Override
                public Iterator<String> iterator () {
                  return asList ("r1", "r2", "r3").iterator ();
                }

                @Override
                public String get (int index) {
                  return "r" + index;
                }
              });
              put ("column", new Dimension<String> () {
                public int size () {
                  return 4;
                }

                public String name () {
                  return "column";
                }

                @Override
                public Iterator<String> iterator () {
                  return asList ("c1", "c2", "c3", "c4").iterator ();
                }

                @Override
                public String get (int index) {
                  return "c" + index;
                }
              });
            }
          };
        }

        @Override
        public Map<String, Analysis> analyses () {
          return new HashMap<> ();
        }

        @Override
        public Values<String, Double> values () {
          return new Values<String, Double> () {
            private Map<String, String> coordinates (String row, String column) {
              return of ("row", row, "column", column);
            }

            @Override
            public Iterable<Value<String, Double>> get (Iterable<Map<String, String>> coordinates) throws InvalidCoordinateSetException {
              return asList (new Value<String, Double> (.0, coordinates ("r1", "c1")),
                             new Value<String, Double> (.1, coordinates ("r1", "c2")),
                             new Value<String, Double> (.2, coordinates ("r1", "c3")),
                             new Value<String, Double> (.3, coordinates ("r1", "c4")),
                             new Value<String, Double> (-.1, coordinates ("r2", "c1")),
                             new Value<String, Double> (-.2, coordinates ("r2", "c2")),
                             new Value<String, Double> (-.3, coordinates ("r2", "c3")),
                             new Value<String, Double> (-.4, coordinates ("r2", "c4")),
                             new Value<String, Double> (.11, coordinates ("r3", "c1")),
                             new Value<String, Double> (-.22, coordinates ("r3", "c2")),
                             new Value<String, Double> (.33, coordinates ("r3", "c3")),
                             new Value<String, Double> (-.44, coordinates ("r3", "c4")));
            }
          };
        }
      }),
                is ("{\"name\":\"mock\",\"dimensions\":[{\"name\":\"row\",\"keys\":[\"r"
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
                    + "\":-0.44}],\"analyses\":[]}"));
  }
}
