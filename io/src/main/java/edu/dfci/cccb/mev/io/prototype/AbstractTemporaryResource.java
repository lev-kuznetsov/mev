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
package edu.dfci.cccb.mev.io.prototype;

import static java.lang.System.getProperty;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.walkFileTree;
import static java.nio.file.Paths.get;
import static java.util.UUID.randomUUID;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author levk
 * 
 */
public abstract class AbstractTemporaryResource extends File implements Closeable {
  private static final long serialVersionUID = 1L;

  private final static File PARENT = new File (getProperty (AbstractTemporaryResource.class.getName () + ".PARENT",
                                                            getProperty ("java.io.tmpdir")));

  protected AbstractTemporaryResource (String prefix, String suffix) {
    super (PARENT, prefix + randomUUID () + suffix);
  }

  protected AbstractTemporaryResource (String suffix) {
    this ("", suffix);
  }

  protected AbstractTemporaryResource () {
    this ("");
  }

  /* (non-Javadoc)
   * @see java.io.Closeable#close() */
  @Override
  public void close () throws IOException {
    final IOException toThrow = new IOException ("Failure while cleaning temporary resource");
    walkFileTree (get (toURI ()), new SimpleFileVisitor<Path> () {
      /* (non-Javadoc)
       * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object,
       * java.nio.file.attribute.BasicFileAttributes) */
      @Override
      public FileVisitResult visitFile (Path file, BasicFileAttributes attrs) throws IOException {
        try {
          Files.delete (file);
        } catch (IOException e) {
          toThrow.addSuppressed (e);
        }
        return CONTINUE;
      }

      /* (non-Javadoc)
       * @see
       * java.nio.file.SimpleFileVisitor#postVisitDirectory(java.lang.Object,
       * java.io.IOException) */
      @Override
      public FileVisitResult postVisitDirectory (Path dir, IOException exc) throws IOException {
        try {
          Files.delete (dir);
        } catch (IOException e) {
          toThrow.addSuppressed (e);
        }
        return CONTINUE;
      }
    });
    if (toThrow.getSuppressed ().length > 0)
      throw toThrow;
  }
}
