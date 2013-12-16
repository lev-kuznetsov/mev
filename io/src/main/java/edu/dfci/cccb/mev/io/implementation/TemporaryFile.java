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
package edu.dfci.cccb.mev.io.implementation;

import java.io.IOException;

import edu.dfci.cccb.mev.io.prototype.AbstractTemporaryResource;

/**
 * @author levk
 * 
 */
public class TemporaryFile extends AbstractTemporaryResource {
  private static final long serialVersionUID = 1L;

  /**
   * @param prefix
   * @param suffix
   */
  public TemporaryFile (String prefix, String suffix) throws IOException {
    super (prefix, suffix);
    createNewFile ();
  }

  /**
   * @param suffix
   */
  public TemporaryFile (String suffix) throws IOException {
    super (suffix);
    createNewFile ();
  }

  /**
   * 
   */
  public TemporaryFile () throws IOException {
    createNewFile ();
  }
}
