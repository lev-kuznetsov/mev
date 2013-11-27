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
package edu.dfci.cccb.mev.dataset.domain.contract;

import java.util.List;

/**
 * @author levk
 * 
 */
public interface Dimension {

  Type type ();

  List<String> keys ();

  Selections selections ();

  Annotation annotation ();

  public enum Type {
    ROW ("row", "y", "1", "gene", "probe", "height"),
    COLUMN ("column", "col", "sample", "0", "x", "width");

    private Type (String... aliases) {
      this.aliases = aliases;
    }

    private final String[] aliases;

    public static Type from (String string) throws InvalidDimensionTypeException {
      for (Type type : values ())
        for (String alias : type.aliases)
          if (alias.equalsIgnoreCase (string))
            return type;
      throw new InvalidDimensionTypeException ().dimension (string);
    }

    @Override
    public String toString () {
      return aliases[0];
    }
  }
}
