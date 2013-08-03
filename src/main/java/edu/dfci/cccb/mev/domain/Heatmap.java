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
package edu.dfci.cccb.mev.domain;

import static edu.dfci.cccb.mev.domain.MatrixAnnotation.Meta.CATEGORICAL;
import static edu.dfci.cccb.mev.domain.MatrixAnnotation.Meta.QUANTITATIVE;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Synchronized;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author levk
 * 
 */
public class Heatmap implements Closeable {

  private RealMatrix data;
  private List<Map<String, ?>> rowAnnotations = new ArrayList<Map<String, ?>> ();
  private List<Map<String, ?>> columnAnnotations = new ArrayList<Map<String, ?>> ();
  private List<Map<String, Map<String, String>>> rowSelections = new SelectionHolderList ();
  private List<Map<String, Map<String, String>>> columnSelections = new SelectionHolderList ();

  /**
   * Gets subset of the data
   * 
   * @param startRow
   * @param endRow
   * @param startColumn
   * @param endColumn
   * @return
   */
  public MatrixData getData (int startRow, int endRow, int startColumn, int endColumn) {
    if (startRow >= data.getRowDimension () || startColumn >= data.getColumnDimension ())
      return EMPTY_MATRIX_DATA;
    startRow = max (startRow, 0);
    startRow = min (startRow, data.getRowDimension () - 1);
    endRow = max (endRow, startRow);
    endRow = min (endRow, data.getColumnDimension () - 1);
    return new MatrixData (data.getSubMatrix (startRow, endRow, startColumn, endColumn));
  }

  /**
   * Get available row annotation types
   * 
   * @return
   */
  public Collection<String> getRowAnnotationTypes () {
    return getAnnotationTypes (rowAnnotations);
  }

  /**
   * Get available column annotation types
   * 
   * @return
   */
  public Collection<String> getColumnAnnotationTypes () {
    return getAnnotationTypes (columnAnnotations);
  }

  /**
   * Get subset of row annotations
   * 
   * @param start
   * @param end
   * @param type
   * @return
   */
  public MatrixAnnotation<?> getRowAnnotation (int index,
                                               String type) throws AnnotationNotFoundException {
    return getAnnotation (rowAnnotations, index, type);
  }

  /**
   * Get subset of column annotations
   * 
   * @param start
   * @param end
   * @param type
   * @return
   */
  public MatrixAnnotation<?> getColumnAnnotation (int index,
                                                  String type) throws AnnotationNotFoundException {
    return getAnnotation (columnAnnotations, index, type);
  }

  public Collection<String> getRowSelectionIds () {
    return getSelectionIds (rowSelections);
  }
  
  public Collection<String> getColumnSelectionIds () {
    return getSelectionIds (columnSelections);
  }
  
  public MatrixSelection getRowSelection (String id, int start, int end) {
    return getSelection (rowSelections, start, end, id);
  }

  public MatrixSelection getColumnSelection (String id, int start, int end) {
    return getSelection (columnSelections, start, end, id);
  }

  public void setRowSelection (String id, MatrixSelection selection) throws IndexOutOfBoundsException {
    setSelection (rowSelections, id, selection);
  }

  public void setColumnSelection (String id, MatrixSelection selection) throws IndexOutOfBoundsException {
    setSelection (columnSelections, id, selection);
  }

  public void deleteRowSelection (String id) {
    deleteSelection (rowSelections, id);
  }

  public void deleteColumnSelections (String id) {
    deleteSelection (columnSelections, id);
  }

  /* (non-Javadoc)
   * @see java.io.Closeable#close() */
  @Override
  public void close () throws IOException {
    if (data instanceof Closeable)
      ((Closeable) data).close ();
  }

  public static class Builder {

  }

  private Collection<String> getAnnotationTypes (List<Map<String, ?>> dimmension) {
    Set<String> result = new HashSet<String> ();
    for (Map<String, ?> entry : dimmension)
      result.addAll (entry.keySet ());
    return result;
  }

  @SuppressWarnings ({ "rawtypes", "unchecked" })
  private MatrixAnnotation<?> getAnnotation (List<Map<String, ?>> dimension,
                                             int index,
                                             String type) throws AnnotationNotFoundException {
    if (index < 0 || index >= dimension.size ())
      throw new IndexOutOfBoundsException ();
    Number min = Double.MAX_VALUE;
    Number max = Double.MIN_VALUE;
    Set<Object> categorical = new HashSet<> ();
    boolean isQuantitative = true;
    for (Map<String, ?> entry : dimension) {
      Object value = entry.get (type);
      if (value == null)
        throw new AnnotationNotFoundException (type);
      if (isQuantitative)
        if (value instanceof Number) {
          Number number = (Number) value;
          if (min.doubleValue () >= number.doubleValue ())
            min = number;
          if (max.doubleValue () <= number.doubleValue ())
            max = number;
        } else
          isQuantitative = false;
      categorical.add (value);
    }
    return new MatrixAnnotation (type,
                                 dimension.get (index).get (type),
                                 isQuantitative ? QUANTITATIVE : CATEGORICAL,
                                 isQuantitative ? asList (min, max) : categorical);
  }

  private Collection<String> getSelectionIds (List<Map<String, Map<String, String>>> dimension) {
    Set<String> result = new HashSet<> ();
    for (Map<String, Map<String, String>> index : dimension)
      result.addAll (index.keySet ());
    return result;
  }
  
  private MatrixSelection getSelection (List<Map<String, Map<String, String>>> dimension, int start, int end, String id) {
    List<Integer> indecis = new ArrayList<Integer> ();
    Map<String, String> attributes = null;
    for (int index = end; --index >= start;)
      if ((attributes = dimension.get (index).get (id)) != null)
        indecis.add (index);
    return new MatrixSelection (attributes, indecis);
  }

  private void setSelection (List<Map<String, Map<String, String>>> dimension, String id, MatrixSelection selection) {
    for (int index : selection.indecis ())
      dimension.get (index).put (id, selection.attributes ());
  }

  private void deleteSelection (List<Map<String, Map<String, String>>> dimension, String id) {
    for (Map<String, Map<String, String>> selections : dimension)
      selections.remove (id);
  }

  private static final MatrixData EMPTY_MATRIX_DATA = new MatrixData (null) {

    @Override
    public int rows () {
      return 0;
    }

    @Override
    public int columns () {
      return 0;
    }

    @Override
    public List<Double> values () {
      return emptyList ();
    }
  };

  private class SelectionHolderList extends ArrayList<Map<String, Map<String, String>>> {
    private static final long serialVersionUID = 1L;

    @Override
    @Synchronized
    public Map<String, Map<String, String>> get (int index) {
      while (size () < index)
        add (null);
      Map<String, Map<String, String>> result = super.get (index);
      if (result == null)
        set (index, result = new HashMap<String, Map<String, String>> ());
      return result;
    }
  }
}
