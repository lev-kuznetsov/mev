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
package edu.dfci.cccb.mev.hcl.domain.concrete;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.ROW;
import static java.util.Arrays.asList;

import java.util.AbstractList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.apache.commons.math3.linear.RealMatrix;
import org.javatuples.Pair;

import com.google.common.cache.Cache;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.hcl.domain.contract.Algorithm;
import edu.dfci.cccb.mev.hcl.domain.contract.Leaf;
import edu.dfci.cccb.mev.hcl.domain.contract.Metric;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;
import edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder;
import edu.dfci.cccb.mev.hcl.domain.prototype.AbstractHcl;
import edu.dfci.cccb.mev.math.commons.contract.RealMatrixBuilder;

/**
 * @author levk
 * 
 */
@ToString
@EqualsAndHashCode (callSuper = true)
@Log4j
public class TwoDimensionalCommonsMathRealMatrixHcl <T> extends AbstractHcl<T> {

  private @Getter @Setter (onMethod = @_ (@Inject)) RealMatrixBuilder matrixBuilder;
  private @Getter @Setter (onMethod = @_ (@Inject)) NodeBuilder nodeBuilder;
  private Comparator<T> comparator;

  @Inject
  public void setComparatorProvider (Provider<Comparator<T>> provider) {
    comparator = provider.get ();
    if (comparator == null)
      comparator = new Comparator<T> () {
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object,
         * java.lang.Object) */
        @SuppressWarnings ("unchecked")
        @Override
        public int compare (T o1, T o2) {
          return ((Comparable<T>) o1).compareTo (o2);
        }
      };
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.hcl.domain.contract.Hcl#cluster(edu.dfci.cccb.mev.dataset
   * .domain.contract.Dataset,
   * edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type,
   * edu.dfci.cccb.mev.hcl.domain.contract.Metric) */
  @Override
  public Node<T> cluster (final Dataset dataset, Dimension dimension, Metric<T> metric, Algorithm<T> algorithm) throws DatasetException {
    RealMatrix distances = null;
    try {
      NodeBag nodes = new NodeBag (dataset, dimension, metric);
      for (String key : dimension.keys ())
        nodes.leaf (key);

      for (;;) {
        Entry<T, Pair<Node<T>, Node<T>>> closest = nodes.closest ();
        Node<T> aggregate = nodeBuilder.distance (closest.getKey ())
                                       .children (createChildrenSet (closest.getValue ().getValue0 (),
                                                                     closest.getValue ().getValue1 ()));
        if (nodes.size () <= 0)
          return aggregate;

      }
    } finally {
      if (distances instanceof AutoCloseable)
        try {
          ((AutoCloseable) distances).close ();
        } catch (Exception e) {
          log.warn ("Exception while closing distances matrix for hierarchical clustering of dataset " + dataset, e);
        }
    }
  }

  private Set<Node> createChildrenSet (final Node... nodes) {
    return new HashSet<> (asList (nodes));
  }

  private class NodeBag {

    private final Metric<T> metric;
    private final Dataset dataset;
    private final Dimension dimension;

    private Cache<String, Leaf<T>> leaves = newBuilder ().build ();
    private TreeMap<T, Pair<Node<T>, Node<T>>> sorted = new TreeMap<> (comparator);

    public NodeBag (Dataset dataset, Dimension dimension, Metric<T> metric) {
      this.metric = metric;
      this.dataset = dataset;
      this.dimension = dimension;
    }

    @SneakyThrows (ExecutionException.class)
    public void leaf (final String name) throws InvalidDimensionTypeException {
      List<Double> from = vectorProjection (dataset, dimension, name);
      for (final String key : leaves.asMap ().keySet ())
        sorted.put (metric.distance (from, vectorProjection (dataset, dimension, key)),
                    new Pair<Node<T>, Node<T>> (leaves.get (name, new Callable<Leaf<T>> () {

                      @Override
                      public Leaf<T> call () throws Exception {
                        return nodeBuilder.distance (0.0).name (name);
                      }
                    }), leaves.getIfPresent (key)));
    }

    public Entry<T, Pair<Node<T>, Node<T>>> closest () {
      Iterator<Entry<T, Pair<Node<T>, Node<T>>>> i = sorted.entrySet ().iterator ();
      if (i.hasNext ()) {
        Entry<T, Pair<Node<T>, Node<T>>> closest = i.next ();
        i.remove ();
        return closest;
      } else
        return null;
    }

    public int size () {
      return sorted.size ();
    }

    private List<Double> vectorProjection (final Dataset dataset, final Dimension dimension, final String key) throws InvalidDimensionTypeException {
      final Dimension other = dataset.dimension (dimension.type () == ROW ? COLUMN : ROW);

      return new AbstractList<Double> () {

        @Override
        @SneakyThrows (InvalidCoordinateException.class)
        public Double get (int index) {
          return dimension.type () == ROW
                                         ? dataset.values ().get (key, other.keys ().get (index))
                                         : dataset.values ().get (other.keys ().get (index), key);
        }

        @Override
        public int size () {
          return other.keys ().size ();
        }
      };
    }
  }
}
