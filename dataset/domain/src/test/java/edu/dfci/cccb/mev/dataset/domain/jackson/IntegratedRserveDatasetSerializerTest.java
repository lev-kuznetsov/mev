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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import edu.dfci.cccb.mev.common.domain.guice.MevModule;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Callback;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Result;
import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.InvalidCoordinateSetException;
import edu.dfci.cccb.mev.dataset.domain.guice.DatasetModule;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.ValuesAdapter;

@RunWith (JukitoRunner.class)
@Category (edu.dfci.cccb.mev.common.domain.jobs.r.R.class)
public class IntegratedRserveDatasetSerializerTest {

  public static class RserveDatasetSerializerTestModule extends JukitoModule {
    protected void configureTest () {
      install (new MevModule ());
      install (new DatasetModule ());
    }
  }

  @R ("function (dataset) class (dataset)")
  public static class Ds {
    private @Parameter Dataset<String, Double> dataset;
    private final CountDownLatch c = new CountDownLatch (1);

    {
      dataset = new DatasetAdapter<> ("test",
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
                                          return new Value<String, Double> (valueOf (coord.get ("row").substring (1))
                                                                            * valueOf (coord.get ("column")
                                                                                            .substring (1)), coord);
                                        }

                                        @Override
                                        public Iterable<Value<String, Double>> get (final Iterable<Map<String, String>> coordinates) throws InvalidCoordinateSetException {
                                          return new Iterable<Value<String, Double>> () {
                                            @Override
                                            public Iterator<Value<String, Double>> iterator () {
                                              return new Iterator<Value<String, Double>> () {
                                                private Iterator<Map<String, String>> coords = coordinates.iterator ();

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
    }

    private @Result String result;

    @Callback
    public void release () {
      c.countDown ();
    }
  }

  @Test
  public void test (edu.dfci.cccb.mev.common.domain.jobs.r.R r, Ds ds) throws Exception {
    r.dispatch (ds);
    ds.c.await ();
    assertThat (ds.result, is ("data.frame"));
  }
}
