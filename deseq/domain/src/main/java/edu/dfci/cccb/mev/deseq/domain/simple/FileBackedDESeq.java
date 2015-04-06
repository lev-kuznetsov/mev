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
package edu.dfci.cccb.mev.deseq.domain.simple;

import static edu.dfci.cccb.mev.deseq.domain.simple.StatelessScriptEngineFileBackedDESeqBuilder.OUTPUT_FILENAME;
import static java.lang.Double.parseDouble;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.ToString;
import org.apache.commons.io.IOUtils;

import edu.dfci.cccb.mev.deseq.domain.prototype.AbstractDESeq;
import edu.dfci.cccb.mev.io.implementation.TemporaryFolder;

/**
 * @author levk
 * 
 */
@ToString
public class FileBackedDESeq extends AbstractDESeq implements AutoCloseable {

  private @Getter final File full;
  private @Getter final TemporaryFolder tempFolder;

  public FileBackedDESeq (TemporaryFolder tempFolder) {
    this.tempFolder = tempFolder;
    this.full = new File (this.tempFolder, OUTPUT_FILENAME);
  }

  @SneakyThrows
  public static FileBackedDESeq from (InputStream results) {
    FileBackedDESeq result = new FileBackedDESeq (new TemporaryFolder ());
    try (OutputStream full = new BufferedOutputStream (new FileOutputStream (result.full));
         BufferedInputStream in = new BufferedInputStream (results)) {
      IOUtils.copy (in, full);
    }
    return result;
  }

  @SneakyThrows
  public void to (OutputStream out) {
    try (InputStream full = new BufferedInputStream (new FileInputStream (this.full));
         OutputStream o = new BufferedOutputStream (out)) {
      IOUtils.copy (full, o);
    }
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.deseq.domain.contract.DESeqResult#full() */
  @Override
  public Iterable<Entry> full () {
    return iterateEntries (full);
  }

  private Iterable<Entry> iterateEntries (final File file) {
    return new Iterable<Entry> () {

      /* (non-Javadoc)
       * @see java.lang.Iterable#iterator() */
      @Override
      @SneakyThrows (IOException.class)
      public Iterator<Entry> iterator () {
        return new Iterator<Entry> () {

          private final BufferedReader reader = new BufferedReader (new FileReader (file));
          private String current = null;

          @Override
          @Synchronized
          @SneakyThrows (IOException.class)
          public boolean hasNext () {
            return current == null ? (current = reader.readLine ()) != null : true;
          }

          @Override
          @Synchronized
          public Entry next () {
            hasNext ();
            Entry result = parse (current);
            current = null;
            return result;
          }

          @Override
          public void remove () {
            throw new UnsupportedOperationException ();
          }
        };
      }
    };
  }

  private Entry parse (String line) {
    final String[] split = line.split ("\t");
    return new SimpleEntry (string (0, split),
                            number (5, split),
                            number (2, split),
                            number (3, split),
                            number (6, split),
                            number (7, split));
  }

  private String string (int index, String[] split) {
    return split[index];
  }

  private Double number (int index, String[] split) {
    String value = string (index, split);
    if ("Inf".equals (value))
      return Double.POSITIVE_INFINITY;
    else if ("-Inf".equals (value))
      return Double.NEGATIVE_INFINITY;
    else if ("NA".equals (value))
      return Double.NaN;
    else
      return parseDouble (value);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#finalize() */
  @Override
  protected void finalize () throws Throwable {
    close ();
  }

  /* (non-Javadoc)
   * @see java.lang.AutoCloseable#close() */
  @Override
  public void close () throws Exception {
    tempFolder.close ();
  }
}
