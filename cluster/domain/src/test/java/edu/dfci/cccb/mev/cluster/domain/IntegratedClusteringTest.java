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

package edu.dfci.cccb.mev.cluster.domain;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import edu.dfci.cccb.mev.cluster.domain.Node.Leaf;
import edu.dfci.cccb.mev.common.domain.guice.MevModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.annotation.Handling;
import edu.dfci.cccb.mev.common.domain.guice.rserve.annotation.Rserve;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Callback;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Result;
import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.InvalidCoordinateSetException;
import edu.dfci.cccb.mev.dataset.domain.jackson.RserveDatasetSerializer;
import edu.dfci.cccb.mev.dataset.domain.prototype.AnalysisAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.ValuesAdapter;

@RunWith (JukitoRunner.class)
@Category (edu.dfci.cccb.mev.common.domain.jobs.r.R.class)
public class IntegratedClusteringTest {

  public static class IntegratedTestModule extends JukitoModule {

    protected void configureTest () {
      install (new MevModule ());
      newSetBinder (binder (), new TypeLiteral<JsonSerializer<?>> () {}, Rserve.class).addBinding ()
                                                                                      .toInstance (new RserveDatasetSerializer<> ());
      bind (new TypeLiteral<Dataset<String, Double>> () {}).toInstance (ds (5, 3000));
      bind (J.class);
      bind (H.class);
      newSetBinder (binder (), new TypeLiteral<Class<? extends Distance>> () {});
      newSetBinder (binder (), new TypeLiteral<Class<? extends Linkage>> () {});
    }
  }

  @R ("function (d) d")
  public static class J {
    private @Parameter Distance d = new Distance.Euclidean ();
    private @Result Map<String, String> r;
    private final CountDownLatch c = new CountDownLatch (1);

    @Callback
    private void cb () {
      c.countDown ();
    }
  }

  @Test
  public void transferDistance (edu.dfci.cccb.mev.common.domain.jobs.r.R r, J j) throws Exception {
    r.dispatch (j);
    j.c.await ();
    assertThat (j.r.get ("type"), equalTo ("euclidean"));
  }

  private static Dataset<String, Double> ds (int rows, int columns) {
    String[] r = new String[rows];
    for (int i = r.length; --i >= 0; r[i] = "r" + (i + 1));
    String[] c = new String[columns];
    for (int i = c.length; --i >= 0; c[i] = "c" + (i + 1));
    return ds ("test", c, r);
  }

  private static Dataset<String, Double> ds (final String name,
                                             final String[] cols,
                                             final String[] rows) {
    final Random rnd = new Random (1);
    return new DatasetAdapter<> (name, DimensionAdapter.dimensions (new DimensionAdapter<String> ("column") {
      public int size () {
        return cols.length;
      }

      public String get (int index) {
        return cols[index];
      }
    }, new DimensionAdapter<String> ("row") {
      public int size () {
        return rows.length;
      }

      public String get (int index) {
        return rows[index];
      }
    }), AnalysisAdapter.analyses (), new ValuesAdapter<String, Double> () {
      public Iterable<Value<String, Double>> get (final Iterable<Map<String, String>> coordinates) throws InvalidCoordinateSetException {
        return new Iterable<Value<String, Double>> () {
          public Iterator<Value<String, Double>> iterator () {
            return new Iterator<Value<String, Double>> () {
              private Iterator<Map<String, String>> coords = coordinates.iterator ();

              public boolean hasNext () {
                return coords.hasNext ();
              }

              public Value<String, Double> next () {
                return new Value<String, Double> (rnd.nextDouble (), coords.next ());
              }

              public void remove () {}
            };
          }
        };
      }
    });
  }

  @R ("function (hclust, dataset, distance, linkage, dimension, subset = NULL) hclust (dataset, distance, linkage, dimension, subset)")
  public static class H {
    private @Parameter Distance distance = new Distance.Euclidean ();
    private @Parameter Dataset<String, Double> dataset = ds (34250, 80);
    private @Parameter Linkage linkage = new Linkage.Average ();
    private @Parameter Map<String, String> dimension = of ("name", "column");
    private @Parameter String[] subset = { "c2", "c3", "c7", "c5" };

    private @Result Node<String> root;

    private final CountDownLatch c = new CountDownLatch (1);

    @Callback
    private void cb () {
      c.countDown ();
    }
  }

  private Set<String> collect (Node<String> root) throws IllegalAccessException, NoSuchFieldException {
    if (root instanceof Leaf) {
      HashSet<String> r = new HashSet<> ();
      Field f = root.getClass ().getDeclaredField ("name");
      f.setAccessible (true);
      r.add ((String) f.get (root));
      return r;
    } else {
      Field l = root.getClass ().getDeclaredField ("left");
      l.setAccessible (true);
      @SuppressWarnings ("unchecked") Set<String> ls = (Set<String>) collect ((Node<String>) l.get (root));
      Field r = root.getClass ().getDeclaredField ("right");
      r.setAccessible (true);
      @SuppressWarnings ("unchecked") Set<String> rs = (Set<String>) collect ((Node<String>) r.get (root));
      ls.addAll (rs);
      return ls;
    }
  }

  @Test
  public void hclust (edu.dfci.cccb.mev.common.domain.jobs.r.R r, H h) throws Exception {
    r.dispatch (h);
    h.c.await ();
    assertThat (collect (h.root).toArray (new String[0]), arrayContaining ("c2", "c5", "c3", "c7"));
  }

  @Test
  public void integratedHclust (edu.dfci.cccb.mev.common.domain.jobs.r.R r, Injector injector) throws Exception {
    final CountDownLatch c = new CountDownLatch (1);
    Hierarchical<String, Double> hcl = new Hierarchical<String, Double> () {
      @Callback
      private void done () {
        c.countDown ();
      }
    };
    System.out.println (injector.getInstance (Key.get (ObjectMapper.class, Handling.Factory.APPLICATION_JSON))
                                .writeValueAsString (injector.getInstance (Key.get (new TypeLiteral<Dataset<String, Double>> () {}))
                                                             .dimensions ()
                                                             .get ("column")));
    injector.injectMembers (hcl);
    long b = System.currentTimeMillis ();
    r.dispatch (hcl);
    c.await ();
    System.out.println ((System.currentTimeMillis () - b) + "ms " + hcl.root ());
  }
}
