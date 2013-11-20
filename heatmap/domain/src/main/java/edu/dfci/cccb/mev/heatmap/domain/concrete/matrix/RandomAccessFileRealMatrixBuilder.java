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
package edu.dfci.cccb.mev.heatmap.domain.concrete.matrix;

import static java.io.File.separator;
import static java.lang.System.getProperty;
import static java.util.UUID.randomUUID;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author levk
 * 
 */
public class RandomAccessFileRealMatrixBuilder implements Closeable {

  private RandomAccessFile file;
  private final String parent;
  private String path;

  private RandomAccessFileRealMatrixBuilder (String parent) throws IOException {
    this.parent = parent;
    init ();
  }

  private void init () throws IOException {
    path = parent + randomUUID ();
    file = new RandomAccessFile (path, "rw");
  }

  public RandomAccessFileRealMatrixBuilder value (double value) throws IOException {
    file.writeDouble (value);
    return this;
  }

  public RandomAccessFileRealMatrix build (int columns) throws IOException {
    RandomAccessFileRealMatrix result;
    try {
      result = new RandomAccessFileRealMatrix (path, columns);
    } catch (IOException e) {
      try {
        new File (path).delete ();
      } catch (Throwable e2) {
        e.addSuppressed (e2);
      }
      throw e;
    }
    try {
      init ();
    } catch (IOException e) {}

    return result;
  }

  private static String folderWithSlash () {
    String result = getProperty (RandomAccessFileRealMatrixBuilder.class.getName () + ".tmpdir",
                                 getProperty ("java.io.tmpdir"));
    if (!result.endsWith (separator))
      result = result + separator;
    return result;
  }

  public static RandomAccessFileRealMatrixBuilder matrix () throws IOException {
    return new RandomAccessFileRealMatrixBuilder (folderWithSlash ());
  }

  /* (non-Javadoc)
   * @see java.io.Closeable#close() */
  @Override
  public void close () throws IOException {
    if (file != null)
      file.close ();
  }
}
