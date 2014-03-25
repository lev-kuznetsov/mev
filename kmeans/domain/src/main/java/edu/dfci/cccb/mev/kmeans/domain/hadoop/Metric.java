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

import org.apache.mahout.common.distance.ChebyshevDistanceMeasure;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.MahalanobisDistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.common.distance.MinkowskiDistanceMeasure;
import org.apache.mahout.common.distance.TanimotoDistanceMeasure;

/**
 * @author levk
 * 
 */
public enum Metric {
  CHEBYSHEV ("chebyshev", "che") {
    @Override
    public DistanceMeasure measurer () {
      return new ChebyshevDistanceMeasure ();
    }
  },
  EUCLIDEAN ("euclidean", "euc") {
    @Override
    public DistanceMeasure measurer () {
      return new EuclideanDistanceMeasure ();
    }
  },
  COSINE ("cosine", "cos") {
    @Override
    public DistanceMeasure measurer () {
      return new CosineDistanceMeasure ();
    }
  },
  MAHALANOBIS ("mahalanobis", "mah") {
    @Override
    public DistanceMeasure measurer () {
      return new MahalanobisDistanceMeasure ();
    }
  },
  MANHATTAN ("manhattan", "man") {
    @Override
    public DistanceMeasure measurer () {
      return new ManhattanDistanceMeasure ();
    }
  },
  MINKOWSKI ("minkowski", "min") {
    @Override
    public DistanceMeasure measurer () {
      return new MinkowskiDistanceMeasure ();
    }
  },
  TANIMOTO ("tanimoto", "tan") {
    @Override
    public DistanceMeasure measurer () {
      return new TanimotoDistanceMeasure ();
    }
  };

  private String[] aliases;

  private Metric (String... aliases) {
    this.aliases = aliases;
  }

  public abstract DistanceMeasure measurer ();

  public static Metric from (String string) {
    for (Metric metric : values ())
      for (String alias : metric.aliases)
        if (alias.equalsIgnoreCase (string))
          return metric;
    throw new IllegalArgumentException (string);
  }
}
