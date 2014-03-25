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
package edu.dfci.cccb.mev.kmeans.domain.hadoop;

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.ROW;
import static org.apache.hadoop.fs.FileSystem.get;
import static org.apache.mahout.clustering.Cluster.CLUSTERED_POINTS_DIR;
import static org.apache.mahout.clustering.kmeans.KMeansDriver.run;

import java.io.File;
import java.io.IOException;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.WeightedVectorWritable;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.AbstractVector;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.io.implementation.TemporaryFolder;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeans;
import edu.dfci.cccb.mev.kmeans.domain.prototype.AbstractKMeans;
import edu.dfci.cccb.mev.kmeans.domain.prototype.AbstractKMeansBuilder;

/**
 * @author levk
 * 
 */
public class HadoopKMeansBuilder extends AbstractKMeansBuilder {

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder#build() */
  @Override
  public KMeans build () throws DatasetException {
    try (TemporaryFolder hadoop = new TemporaryFolder ()) {
      File points = new File (hadoop, "points");
      points.mkdir ();

      Configuration configuration = new Configuration ();
      FileSystem system = get (configuration);
      final Dimension other = dataset ().dimension (dimension ().type () == ROW ? COLUMN : ROW);

      List<NamedVector> vectors = new AbstractList<NamedVector> () {

        @Override
        public NamedVector get (int index) {
          final String vector = dimension ().keys ().get (index);
          return new NamedVector (new AbstractVector (other.keys ().size ()) {

            @Override
            public void setQuick (int index, double value) {
              throw new UnsupportedOperationException ();
            }

            @Override
            public Vector like () {
              return new RandomAccessSparseVector (size ());
            }

            @Override
            public Iterator<Element> iterator () {
              return new Iterator<Element> () {
                private int current = 0;

                @Override
                public boolean hasNext () {
                  return current < other.keys ().size ();
                }

                @Override
                public Element next () {
                  return new Element () {
                    private final int index = current++;

                    @Override
                    public void set (double value) {
                      throw new UnsupportedOperationException ();
                    }

                    @Override
                    public int index () {
                      return index;
                    }

                    @Override
                    @SneakyThrows (InvalidCoordinateException.class)
                    public double get () {
                      return dimension ().type () == ROW ? dataset ().values ().get (vector, other.keys ().get (index))
                                                        : dataset ().values ().get (other.keys ().get (index), vector);
                    }
                  };
                }

                @Override
                public void remove () {
                  throw new UnsupportedOperationException ();
                }
              };
            }

            @Override
            public Iterator<Element> iterateNonZero () {
              return iterator ();
            }

            @Override
            public boolean isSequentialAccess () {
              return true;
            }

            @Override
            public boolean isDense () {
              return true;
            }

            @Override
            @SneakyThrows (InvalidCoordinateException.class)
            public double getQuick (int index) {
              return dimension ().type () == ROW ? dataset ().values ().get (vector, other.keys ().get (index))
                                                : dataset ().values ().get (other.keys ().get (index), vector);
            }

            @Override
            public int getNumNondefaultElements () {
              return other.keys ().size ();
            }

            @Override
            protected Matrix matrixLike (int rows, int columns) {
              throw new UnsupportedOperationException ();
            }
          }, vector);
        }

        @Override
        public int size () {
          return dimension ().keys ().size ();
        }
      };

      // write input
      try (Writer writer = new Writer (system,
                                       configuration,
                                       new Path (new File (points, "file1").getAbsolutePath ()),
                                       LongWritable.class,
                                       VectorWritable.class)) {
        VectorWritable writable = new VectorWritable ();
        long record = 0;
        for (Vector vector : vectors) {
          writable.set (vector);
          writer.append (new LongWritable (record++), writable);
        }
      }

      // prepare clusters
      File clusters = new File (hadoop, "clusters");
      clusters.mkdir ();
      try (Writer writer = new Writer (system,
                                       configuration,
                                       new Path (new File (clusters, "part-00000").getAbsolutePath ()),
                                       Text.class,
                                       Cluster.class)) {
        for (int i = 0; i < k (); i++) {
          Vector vec = vectors.get (i);
          Cluster cluster = new Cluster (vec, i, new EuclideanDistanceMeasure ());
          writer.append (new Text (cluster.getIdentifier ()), cluster);
        }
      }

      File output = new File (hadoop, "output");
      output.mkdir ();

      try {
        run (configuration,
             new Path (points.getAbsolutePath ()),
             new Path (clusters.getAbsolutePath ()),
             new Path (output.getAbsolutePath ()),
             new EuclideanDistanceMeasure (),
             0.001,
             10,
             true,
             false);

        try (Reader reader = new Reader (system,
                                         new Path (new File (new File (output, CLUSTERED_POINTS_DIR),
                                                             "/part-m-00000").getAbsolutePath ()),
                                         configuration)) {
          IntWritable key = new IntWritable ();
          WeightedVectorWritable value = new WeightedVectorWritable ();
          Map<String, Set<String>> result = new HashMap<> ();

          while (reader.next (key, value)) {
            Set<String> cluster = result.get (key.toString ());
            if (cluster == null)
              result.put (key.toString (), cluster = new HashSet<> ());
            cluster.add (((NamedVector) value.getVector ()).getName ());
          }

          return new AbstractKMeans () {}.dataset (dataset ())
                                         .dimension (dimension ())
                                         .clusters (new HashSet<> (result.values ()));
        }
      } catch (ClassNotFoundException | InterruptedException e) {
        throw new DatasetException (e);
      }
    } catch (IOException e) {
      throw new DatasetException (e);
    }
  }
}
