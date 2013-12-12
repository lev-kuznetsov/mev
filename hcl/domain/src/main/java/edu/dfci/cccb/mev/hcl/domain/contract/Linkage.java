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

import static java.lang.Double.MAX_VALUE;

/**
 * @author levk
 * 
 */
public enum Linkage {
  AVERAGE ("average", "avg", "a") {
    /* (non-Javadoc)
     * @see
     * edu.dfci.cccb.mev.hcl.domain.concrete.Linkage#aggregate(java.lang.Iterable
     * ) */
    @Override
    public double aggregate (Iterable<Double> distances) {
      double total = .0;
      int count = 0;
      for (double current : distances) {
        total += current;
        count++;
      }
      return count != 0 ? total / count : .0;
    }
  },
  COMPLETE ("complete", "cmp", "c", "max") {
    /* (non-Javadoc)
     * @see
     * edu.dfci.cccb.mev.hcl.domain.concrete.Linkage#aggregate(java.lang.Iterable
     * ) */
    @Override
    public double aggregate (Iterable<Double> distances) {
      double max = -MAX_VALUE;
      for (double current : distances)
        max = max < current ? current : max;
      return max;
    }
  },
  SINGLE ("single", "sng", "s", "min") {
    /* (non-Javadoc)
     * @see
     * edu.dfci.cccb.mev.hcl.domain.concrete.Linkage#aggregate(java.lang.Iterable
     * ) */
    @Override
    public double aggregate (Iterable<Double> distances) {
      double min = MAX_VALUE;
      for (double current : distances)
        min = min > current ? current : min;
      return min;
    }
  };

  private final String[] aliases;

  private Linkage (String... aliases) {
    this.aliases = aliases;
  }

  public static Linkage from (String string) throws InvalidAlgorithmException {
    for (Linkage linkage : values ())
      for (String alias : linkage.aliases)
        if (alias.equalsIgnoreCase (string))
          return linkage;
    throw new InvalidAlgorithmException ().name (string);
  }

  public abstract double aggregate (Iterable<Double> distances);
}
