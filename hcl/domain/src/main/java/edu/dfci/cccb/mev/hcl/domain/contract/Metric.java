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
package edu.dfci.cccb.mev.hcl.domain.contract;

import static ch.lambdaj.Lambda.sum;
import static org.apache.commons.math3.util.FastMath.abs;
import static org.apache.commons.math3.util.FastMath.pow;
import static org.apache.commons.math3.util.FastMath.sqrt;

import java.util.AbstractList;
import java.util.List;

/**
 * @author levk
 * 
 */
public enum Metric {
  EUCLIDEAN ("euclidean", "euc", "e") {
    /* (non-Javadoc)
     * @see
     * edu.dfci.cccb.mev.hcl.domain.concrete.Metrics#aggregate(java.util.List) */
    @Override
    protected double aggregate (List<Double> correlations) {
      return sqrt (sum (correlations).doubleValue ());
    }

    /* (non-Javadoc)
     * @see edu.dfci.cccb.mev.hcl.domain.concrete.Metrics#correlate(double,
     * double) */
    @Override
    protected double correlate (double from, double to) {
      return pow (from - to, 2);
    }
  },
  MANHATTAN ("manhattan", "man", "m") {
    /* (non-Javadoc)
     * @see
     * edu.dfci.cccb.mev.hcl.domain.concrete.Metrics#aggregate(java.util.List) */
    @Override
    protected double aggregate (List<Double> correlations) {
      return sum (correlations).doubleValue ();
    }

    /* (non-Javadoc)
     * @see edu.dfci.cccb.mev.hcl.domain.concrete.Metrics#correlate(double,
     * double) */
    @Override
    protected double correlate (double from, double to) {
      return abs (from - to);
    }
  },
  PEARSON ("pearson", "pea", "p") {
    /* (non-Javadoc)
     * @see
     * edu.dfci.cccb.mev.hcl.domain.concrete.Metrics#aggregate(java.util.List) */
    @Override
    protected double aggregate (List<Double> correlations) {
      return -sum (correlations).doubleValue () / correlations.size ();
    }

    /* (non-Javadoc)
     * @see edu.dfci.cccb.mev.hcl.domain.concrete.Metrics#correlate(double,
     * double) */
    @Override
    protected double correlate (double from, double to) {
      return from * to;
    }
  };

  private final String[] aliases;

  private Metric (String... aliases) {
    this.aliases = aliases;
  }

  public static Metric from (String string) throws InvalidMetricException {
    for (Metric metric : values ())
      for (String alias : metric.aliases)
        if (alias.equalsIgnoreCase (string))
          return metric;
    throw new InvalidMetricException ().name (string);
  }

  public double distance (List<Double> from, List<Double> to) {
    return aggregate (correlate (from, to));
  }

  protected abstract double aggregate (List<Double> correlations);

  protected List<Double> correlate (final List<Double> from, final List<Double> to) {
    return new AbstractList<Double> () {

      @Override
      public Double get (int index) {
        return correlate (from.get (index), to.get (index));
      }

      @Override
      public int size () {
        return from.size ();
      }
    };
  }

  protected abstract double correlate (double from, double to);
}
