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
public class TemporaryFolder extends AbstractTemporaryResource {
  private static final long serialVersionUID = 1L;

  /**
   * @param prefix
   * @param suffix
   * @throws IOException
   */
  public TemporaryFolder (String prefix, String suffix) throws IOException {
    super (prefix, suffix);
    if (!mkdirs ())
      throw new IOException ("Unable to create temporary folder");
  }

  /**
   * @param suffix
   * @throws IOException
   */
  public TemporaryFolder (String suffix) throws IOException {
    super (suffix);
    if (!mkdirs ())
      throw new IOException ("Unable to create temporary folder");
  }

  /**
   * @throws IOException
   * 
   */
  public TemporaryFolder () throws IOException {
    if (!mkdirs ())
      throw new IOException ("Unable to create temporary folder");
  }

}
