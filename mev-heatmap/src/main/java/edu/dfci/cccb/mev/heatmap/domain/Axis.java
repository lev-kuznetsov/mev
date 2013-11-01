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
package edu.dfci.cccb.mev.heatmap.domain;

/**
 * @author levk
 * 
 */
public enum Axis {
  COLUMN ("column", "0", "x", "width"),
  ROW ("row", "1", "y", "length");

  private final String[] aliases;

  private Axis (String... aliases) {
    this.aliases = aliases;
  }

  public static Axis from (String value) throws InvalidDimensionException {
    for (Axis axis : values ())
      for (String alias : axis.aliases)
        if (alias.equalsIgnoreCase (value))
          return axis;
    throw new InvalidDimensionException ();
  }
}
