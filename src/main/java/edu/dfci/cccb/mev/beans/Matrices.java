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
package edu.dfci.cccb.mev.beans;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.experimental.Accessors;

import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.stereotype.Component;

import us.levk.math.linear.HugeRealMatrix;

/**
 * Bean wrapper for session scoped matrices
 * 
 * @author levk
 * 
 */
@Component
public class Matrices implements Closeable {

  /**
   * Holds references to matrices based on string keys
   */
  private Map<String, MatrixData<? extends RealMatrix>> matrices = new HashMap<> ();

  @Data
  @Accessors (fluent = true, chain = false)
  private class MatrixData <T extends RealMatrix> implements Closeable {
    private final T matrix;

    private final List<Map<String, List<String>>> decorations = new ArrayList<Map<String, List<String>>> () {
      private static final long serialVersionUID = 1L;

      @Override
      @Synchronized
      public Map<String, List<String>> get (int index) {
        for (; index < size () - 1; add (new HashMap<String, List<String>> () {
          private static final long serialVersionUID = 1L;

          @Override
          @Synchronized
          public List<String> get (Object key) {
            List<String> result = super.get (key);
            if (result == null)
              put ((String) key, result = new ArrayList<String> () {
                private static final long serialVersionUID = 1L;

                @Override
                @Synchronized
                public String set (int index, String element) {
                  for (; index < size () - 1; add (null));
                  return super.set (index, element);
                }
              });
            return result;
          }

          @Override
          @Synchronized
          public List<String> put (String key, List<String> value) {
            return super.put (key, value);
          }
        }));
        return super.get (index);
      }
    };

    /* (non-Javadoc)
     * @see java.io.Closeable#close() */
    @Override
    public void close () throws IOException {
      if (matrix instanceof Closeable)
        ((Closeable) matrix).close ();
    }
  }

  /**
   * Gets the names (or types) of all the decorations for a particular matrix
   * 
   * @param matrix
   * @return
   * @throws MatrixNotFoundException
   */
  public Collection<Collection<String>> decorations (String matrix) throws MatrixNotFoundException {
    Collection<Collection<String>> result = new ArrayList<Collection<String>> ();
    for (Map<String, List<String>> dimension : getData (matrix).decorations ())
      result.add (dimension.keySet ());
    return result;
  }

  /**
   * Decorates a particular matrix with the given attributes
   * 
   * @param matrix
   * @param dimension
   * @param name
   * @param index
   * @param decoration
   * @throws MatrixNotFoundException
   */
  public void decorate (String matrix, int dimension, String name, int index, String decoration) throws MatrixNotFoundException {
    getData (matrix).decorations ().get (dimension).get (name).set (index, decoration);
  }

  /**
   * Gets all the decorations for a particular vector
   * 
   * @param matrix
   * @param dimension
   * @param index
   * @return
   * @throws MatrixNotFoundException
   */
  public Collection<String> decoration (String matrix, int dimension, int index) throws MatrixNotFoundException {
    Collection<String> result = new ArrayList<String> ();
    for (Entry<String, List<String>> decoration : getData (matrix).decorations ().get (dimension).entrySet ())
      result.add (decoration.getValue ().get (index));
    return result;
  }

  /**
   * Gets a specific decoration for a particular vector
   * 
   * @param matrix
   * @param dimension
   * @param index
   * @param name
   * @return
   * @throws MatrixNotFoundException
   */
  public String decoration (String matrix, int dimension, int index, String name) throws MatrixNotFoundException {
    return getData (matrix).decorations ().get (dimension).get (name).get (index);
  }

  /**
   * Lists keys available
   * 
   * @return
   */
  public Collection<String> list () {
    return matrices.keySet ();
  }

  /**
   * Gets a particularly keyed matrix or throws {@link MatrixNotFoundException}
   * 
   * @param key
   * @return
   * @throws MatrixNotFoundException
   */
  public RealMatrix get (String key) throws MatrixNotFoundException {
    return getData (key).matrix ();
  }

  /**
   * Binds a particularly keyed matrix, if a key is already bound, previous
   * value is ejected
   * 
   * @param key
   * @param matrix
   * @throws IOException
   */
  @Synchronized
  public <T extends RealMatrix> void put (String key, T matrix) throws IOException {
    eject (matrices.put (key, new MatrixData<T> (matrix)));
  }

  /**
   * Parses stream into a matrix and puts
   * 
   * @param key
   * @param input
   * @throws ParseException
   * @throws IOException
   */
  public void put (String key, InputStream input) throws ParseException, IOException {
    put (key, parse (input));
  }

  /**
   * Adds a matrix, the key is used as a suggestion, actual key used is returned
   * 
   * @param key
   * @param matrix
   * @return
   */
  @Synchronized
  @SneakyThrows (IOException.class /* Will never happen, IOException is part of
                                    * the Closeable on ejection, no ejection
                                    * happens on addition */)
  public <T extends RealMatrix> String add (String key, T matrix) {
    if (matrices.containsKey (key)) {
      int index = key.length ();
      while (Character.isDigit (key.charAt (--index)));
      if (key.charAt (index) == '-')
        key = key.substring (0, index);
      String alias;
      for (int count = 1; matrices.containsKey (alias = key + "-" + count); count++);
      key = alias;
    }
    put (key, matrix);
    return key;
  }

  /**
   * Parses a stream into a matrix and adds
   * 
   * @param key
   * @param input
   * @return
   * @throws IOException
   * @throws ParseException
   */
  public String add (String key, InputStream input) throws IOException, ParseException {
    return add (key, parse (input));
  }

  /**
   * Ejects a matrix
   * 
   * @param key
   * @throws MatrixNotFoundException
   */
  @Synchronized
  public void delete (String key) throws MatrixNotFoundException, IOException {
    if (eject (matrices.remove (key)) == null)
      throw new MatrixNotFoundException (key);
  }

  /* (non-Javadoc)
   * @see java.io.Closeable#close() */
  @Override
  public void close () throws IOException {
    for (MatrixData<?> matrix : matrices.values ())
      eject (matrix);
  }

  /**
   * Clean up for ejected matrices
   * 
   * @param ejected
   * @return
   * @throws IOException
   */
  private MatrixData<?> eject (MatrixData<?> ejected) throws IOException {
    if (ejected != null && ejected.matrix () instanceof Closeable)
      ((Closeable) ejected.matrix ()).close ();
    return ejected;
  }

  /**
   * Parses input into a matrix
   * 
   * @param input
   * @return
   * @throws ParseException
   * @throws IOException
   */
  // TODO: Do this with a configurable builder instead
  private HugeRealMatrix parse (InputStream input) throws ParseException, IOException {
    return new HugeRealMatrix (input,
                               "\t,".toCharArray (),
                               "\n".toCharArray (),
                               NumberFormat.getNumberInstance ());
  }

  private MatrixData<?> getData (String key) throws MatrixNotFoundException {
    MatrixData<?> result = matrices.get (key);
    if (result == null)
      throw new MatrixNotFoundException (key);
    else
      return result;
  }
}
