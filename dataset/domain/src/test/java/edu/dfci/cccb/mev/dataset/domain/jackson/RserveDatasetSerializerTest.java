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

package edu.dfci.cccb.mev.dataset.domain.jackson;

import static edu.dfci.cccb.mev.dataset.domain.prototype.AnalysisAdapter.analyses;
import static edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter.dimensions;
import static java.lang.Double.valueOf;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.common.domain.guice.MevModule;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.InvalidCoordinateSetException;
import edu.dfci.cccb.mev.dataset.domain.guice.DatasetModule;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.ValuesAdapter;

@RunWith (JukitoRunner.class)
public class RserveDatasetSerializerTest {

  public static class RserveDatasetSerializerTestModule extends JukitoModule {
    protected void configureTest () {
      install (new MevModule ());
      install (new DatasetModule ());
    }
  }

  @Test
  public void test (ObjectMapper mapper) throws Exception {
    Dataset<String, Double> dataset =
                                      new DatasetAdapter<> ("test",
                                                            dimensions ((Dimension<String>) new DimensionAdapter<String> ("row") {

                                                                          @Override
                                                                          public int size () {
                                                                            return 5;
                                                                          }

                                                                          @Override
                                                                          public String get (int index) {
                                                                            return "r" + (index + 1);
                                                                          }
                                                                        },
                                                                        new DimensionAdapter<String> ("column") {

                                                                          @Override
                                                                          public int size () {
                                                                            return 6;
                                                                          }

                                                                          @Override
                                                                          public String get (int index) {
                                                                            return "c" + (index + 1);
                                                                          }
                                                                        }),
                                                            analyses (),
                                                            new ValuesAdapter<String, Double> () {
                                                              private Value<String, Double> toValue (Map<String, String> coord) {
                                                                return new Value<String, Double> (valueOf (coord.get ("row")
                                                                                                                .substring (1))
                                                                                                          * valueOf (coord.get ("column")
                                                                                                                          .substring (1)),
                                                                                                  coord);
                                                              }

                                                              @Override
                                                              public Iterable<Value<String, Double>> get (final Iterable<Map<String, String>> coordinates) throws InvalidCoordinateSetException {
                                                                return new Iterable<Value<String, Double>> () {
                                                                  @Override
                                                                  public Iterator<Value<String, Double>> iterator () {
                                                                    return new Iterator<Value<String, Double>> () {
                                                                      private final Iterator<Map<String, String>> coords =
                                                                                                                           coordinates.iterator ();

                                                                      @Override
                                                                      public boolean hasNext () {
                                                                        return coords.hasNext ();
                                                                      }

                                                                      @Override
                                                                      public Value<String, Double> next () {
                                                                        return toValue (coords.next ());
                                                                      }

                                                                      @Override
                                                                      public void remove () {}
                                                                    };
                                                                  }
                                                                };
                                                              }
                                                            });
    ByteArrayOutputStream buffer = new ByteArrayOutputStream ();
    new RserveDatasetSerializer<String, Double> ().serialize (dataset,
                                                              mapper.getFactory ().createGenerator (buffer),
                                                              mapper.getSerializerProvider ());
    assertEquals ("[{c1:1.0,c2:2.0,c3:3.0,c4:4.0,c5:5.0,c6:6.0,_row:'r1'},"
                  + "{c1:2.0,c2:4.0,c3:6.0,c4:8.0,c5:10.0,c6:12.0,_row:'r2'}"
                  + ",{c1:3.0,c2:6.0,c3:9.0,c4:12.0,c5:15.0,c6:18.0,_row:'r3'}"
                  + ",{c1:4.0,c2:8.0,c3:12.0,c4:16.0,c5:20.0,c6:24.0,_row:'r4'}"
                  + ",{c1:5.0,c2:10.0,c3:15.0,c4:20.0,c5:25.0,c6:30.0,_row:'r5'}]",
                  buffer.toString (),
                  false);
  }
}
