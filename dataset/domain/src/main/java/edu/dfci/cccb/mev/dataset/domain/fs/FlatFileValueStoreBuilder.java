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
package edu.dfci.cccb.mev.dataset.domain.fs;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValueStoreBuilder;
import edu.dfci.cccb.mev.io.implementation.TemporaryFile;

/**
 * @author levk
 * 
 */
@Log4j
public class FlatFileValueStoreBuilder extends AbstractValueStoreBuilder implements Closeable {

  private final File file;
  private RandomAccessFile writer;
  private final Map<String, Integer> rows = new LinkedHashMap<String, Integer> ();
  private final Map<String, Integer> columns = new LinkedHashMap<String, Integer> ();

  public FlatFileValueStoreBuilder () throws IOException {
    file = new TemporaryFile ();
    log.debug ("Created TEMP FILE:" + file.getAbsolutePath ());
    writer = new RandomAccessFile (file, "rw");
  }
  
  public FlatFileValueStoreBuilder (File file) throws IOException {
    this.file = file;
    writer = new RandomAccessFile (file, "rw");
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder#add(double,
   * java.lang.String, java.lang.String) */
  @Override
  public ValueStoreBuilder add (double value, String row, String column) throws ValueStoreException {
    try {
      writer.writeDouble (value);

//      if (!rows.containsKey (row))
//        rows.put (row, rows.size ());
//
//      if (!columns.containsKey (column))
//        columns.put (column, columns.size ());

      return this;
    } catch (IOException e) {
      throw new ValueStoreException (e);
    }
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder#build() */
  @Override
  @Synchronized
  public Values build () {
    if (writer == null)
      throw new IllegalStateException ();
    try {
      writer.close ();
      writer = null;
      return new FlatFileValues (file, rows, columns, rows.size (), columns.size ());
    } catch (IOException e) {
      throw new RuntimeException (e);
    }
  }
  
  @Synchronized
  public Values build (Map<String, Integer> rows, Map<String, Integer> columns) {
    if (writer == null)
      throw new IllegalStateException ();
    try {
      writer.close ();
      writer = null;
      
      return new FlatFileValues (file, rows, columns, rows.size (), columns.size ());
    } catch (IOException e) {
      throw new RuntimeException (e);
    }
  }

  /* (non-Javadoc)
   * @see java.io.Closeable#close() */
  @Override
  @Synchronized
  public void close () throws IOException {
    if (writer != null) {
      try {
        writer.close ();
      } finally {
        if (file instanceof Closeable) ((Closeable)file).close ();
      }
    }
  }
}
