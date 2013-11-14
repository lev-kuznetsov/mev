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

import static java.lang.Math.min;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;
import static java.util.UUID.randomUUID;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.SneakyThrows;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author levk
 * 
 */
public class RandomAccessFileRealMatrix extends AbstractRealMatrix implements Closeable {

  private static final int MAPPING_SIZE = 1 << 30;
  private static final int MAX_CELLS_IN_TO_STRING = 100;

  private final String name;
  private final RandomAccessFile file;
  private @Getter final int columnDimension;
  private @Getter int rowDimension;
  private final List<MappedByteBuffer> mappings = new ArrayList<> ();

  /**
   * Construct an empty matrix
   * 
   * @param width
   * @param height
   * @throws IOException
   * @throws NotStrictlyPositiveException
   */
  public RandomAccessFileRealMatrix (int width, int height) throws IOException, NotStrictlyPositiveException {
    if (width < 1)
      throw new NotStrictlyPositiveException (width);
    else if (height < 1)
      throw new NotStrictlyPositiveException (height);
    try (RandomAccessFile file = new RandomAccessFile (name = randomFileName (), "rw")) {
      this.file = file;
      this.columnDimension = width;
      this.rowDimension = height;
      long size = 8L * height * width;
      for (long offset = 0; offset < size; offset += MAPPING_SIZE)
        mappings.add (file.getChannel ().map (READ_WRITE, offset, min (size - offset, MAPPING_SIZE)));
    }
  }

  /**
   * Creates a matrix from an iterator of values
   * 
   * @param values
   * @param columns
   * @throws IOException
   */
  public RandomAccessFileRealMatrix (Iterator<Double> values, int columns) throws IOException {
    String name = randomFileName ();
    try (RandomAccessFile file = new RandomAccessFile (name, "rw")) {
      long count = 0;
      for (; values.hasNext (); count++)
        file.writeDouble (values.next ());
      if (count % columns != 0)
        throw new IOException ("Uneven column length, total values = "
                               + count + " column length = " + columns + " remainder = " + (count % columns));
      this.name = name;
      this.file = file;
      this.columnDimension = columns;
      this.rowDimension = (int) (count / columns);
      long size = 8L * rowDimension * columnDimension;
      for (long offset = 0; offset < size; offset += MAPPING_SIZE)
        mappings.add (file.getChannel ().map (READ_WRITE, offset, min (size - offset, MAPPING_SIZE)));
    } catch (IOException | RuntimeException | Error e) {
      new File (name).delete ();
      throw e;
    }
  }

  public RandomAccessFileRealMatrix (String path, int columns) throws IOException {
    RandomAccessFile file = new RandomAccessFile (path, "rw");
    this.name = path;
    this.file = file;
    this.columnDimension = columns;
    long size = file.length ();
    this.rowDimension = (int) ((size / 8 /* sizeof(double) */) / columns);
    if ((long) columnDimension * (long) rowDimension != size / 8L)
      throw new IOException ("File size to dimension mismatch, "
                             + columnDimension + " * " + rowDimension + " != " + (size / 8L));
    for (long offset = 0; offset < size; offset += MAPPING_SIZE)
      mappings.add (file.getChannel ().map (READ_WRITE, offset, min (size - offset, MAPPING_SIZE)));
  }

  /* (non-Javadoc)
   * @see java.io.Closeable#close() */
  @Override
  public void close () throws IOException {
    file.close ();
    new File (name).delete ();
  }

  /* (non-Javadoc)
   * @see org.apache.commons.math3.linear.AbstractRealMatrix#createMatrix(int,
   * int) */
  @Override
  @SneakyThrows (IOException.class)
  public RandomAccessFileRealMatrix createMatrix (int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
    return new RandomAccessFileRealMatrix (columnDimension, rowDimension);
  }

  /* (non-Javadoc)
   * @see org.apache.commons.math3.linear.AbstractRealMatrix#copy() */
  @Override
  public RealMatrix copy () {
    RandomAccessFileRealMatrix result = createMatrix (rowDimension, columnDimension);
    for (int row = rowDimension; --row >= 0;)
      for (int column = columnDimension; --column >= 0;) {
        double value = getEntry (row, column);
        if (Double.compare (value, 0.0) != 0)
          result.setEntry (row, column, value);
      }
    return result;
  }

  /* (non-Javadoc)
   * @see org.apache.commons.math3.linear.AbstractRealMatrix#getEntry(int, int) */
  @Override
  public double getEntry (int row, int column) throws OutOfRangeException {
    long position = position (column, row);
    return mappings.get ((int) (position / MAPPING_SIZE)).getDouble ((int) (position % MAPPING_SIZE));
  }

  /* (non-Javadoc)
   * @see org.apache.commons.math3.linear.AbstractRealMatrix#setEntry(int, int,
   * double) */
  @Override
  public void setEntry (int row, int column, double value) throws OutOfRangeException {
    long position = position (column, row);
    mappings.get ((int) (position / MAPPING_SIZE)).putDouble ((int) (position % MAPPING_SIZE), value);
  }

  /* (non-Javadoc)
   * @see org.apache.commons.math3.linear.AbstractRealMatrix#toString() */
  @Override
  public String toString () {
    long num = new File (name).length ();
    String[] suffix = new String[] { " Bytes", " KB", " MB", " GB", " TB" };
    int index = 0;
    for (; num > 1024 && index < suffix.length; num /= 1024, index++);
    String size = num + suffix[index];
    return rowDimension * columnDimension > MAX_CELLS_IN_TO_STRING
                                                                  ? getClass ().getSimpleName ()
                                                                    + "(" + columnDimension + " column"
                                                                    + (columnDimension == 1 ? "" : "s")
                                                                    + ", " + rowDimension + " row"
                                                                    + (rowDimension == 1 ? "" : "s") + ", "
                                                                    + size + " table file)"
                                                                  : super.toString ();
  }

  /**
   * Returns position of the cell contents in the backing file
   * 
   * @param x
   * @param y
   * @return
   * @throws OutOfRangeException
   * @throws IllegalStateException
   */
  private long position (int x, int y) throws OutOfRangeException, IllegalStateException {
    checkRow (y);
    checkColumn (x);
    return (((long) y) * columnDimension + x) * 8;
  }

  /**
   * Checks if the requested row is inbounds
   * 
   * @param row
   * @throws OutOfRangeException
   */
  protected final void checkRow (int row) throws OutOfRangeException {
    checkRange (row, 0, rowDimension);
  }

  /**
   * Checks if the requested column is inbounds
   * 
   * @param column
   * @throws OutOfRangeException
   */
  protected final void checkColumn (int column) throws OutOfRangeException {
    checkRange (column, 0, columnDimension);
  }

  /**
   * Checks if the passed number is within the allowed range
   * 
   * @param num
   * @param lo
   * @param hi
   * @throws OutOfRangeException
   */
  private static void checkRange (int num, int lo, int hi) throws OutOfRangeException {
    if (num < lo || num > hi)
      throw new OutOfRangeException (num, lo, hi);
  }

  /**
   * Generates a unique file name
   * 
   * @return
   */
  private static String randomFileName () {
    return System.getProperty (RandomAccessFileRealMatrix.class.getName () + ".tmpdir",
                               System.getProperty ("java.io.tmpdir")) + randomUUID () + ".matrix";
  }
}
