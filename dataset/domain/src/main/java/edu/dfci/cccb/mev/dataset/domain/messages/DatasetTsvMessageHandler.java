/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.dataset.domain.messages;

import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.join;
import static com.google.common.collect.ImmutableSet.of;
import static edu.dfci.cccb.mev.dataset.domain.prototype.AnalysisAdapter.analyses;
import static edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter.dimensions;
import static java.io.File.createTempFile;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.parseDouble;
import static java.util.Arrays.fill;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.InvalidCoordinateSetException;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter;
import edu.dfci.cccb.mev.dataset.domain.support.binary.FlatFileStoreValuesAdapter;

/**
 * @author levk
 * @since ASHA
 */
@javax.ws.rs.ext.Provider
@Consumes (DatasetTsvMessageHandler.TEXT_TSV)
@Produces (DatasetTsvMessageHandler.TEXT_TSV)
@Log4j
public class DatasetTsvMessageHandler implements MessageBodyReader<Dataset<String, Double>>, MessageBodyWriter<Dataset<String, Double>> {

  public static final MediaType TEXT_TSV_TYPE = new MediaType ("text", "tab-separated-values");
  public static final String TEXT_TSV = "text/tab-separated-values";

  public static final String ROW = "mev.dataset.dimension.row.name";
  public static final String COLUMN = "mev.dataset.dimension.column.name";
  public static final String COMMENT_EXPRESSION = "mev.dataset.parser.comment-expression";
  public static final String END_OF_LINE_SYMBOL = "mev.dataset.parser.end-of-line-symbol";
  public static final String DELIMITER_CHARACTER = "mev.dataset.parser.delimiter-character";
  public static final String QUOTE_CHARACTER = "mev.dataset.parser.quote-character";

  private @Inject CsvPreference preference;
  private @Inject @edu.dfci.cccb.mev.dataset.domain.annotation.Dataset Provider<String> datasetName;
  private @Inject @Named (ROW) String row;
  private @Inject @Named (COLUMN) String column;

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType) */
  @Override
  public boolean isWriteable (Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return Dataset.class.equals (type) && TEXT_TSV_TYPE.equals (mediaType);
  }

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object,
   * java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType) */
  @Override
  public long getSize (Dataset<String, Double> t,
                       Class<?> type,
                       Type genericType,
                       Annotation[] annotations,
                       MediaType mediaType) {
    return -1;
  }

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType) */
  @Override
  public boolean isReadable (Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return type.equals (Dataset.class) && TEXT_TSV_TYPE.equals (mediaType);
  }

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.InputStream) */
  @Override
  public Dataset<String, Double> readFrom (Class<Dataset<String, Double>> type,
                                           Type genericType,
                                           Annotation[] annotations,
                                           MediaType mediaType,
                                           MultivaluedMap<String, String> httpHeaders,
                                           InputStream input) throws IOException, WebApplicationException {
    File values = createTempFile ("mev.", ".data");

    try (DataOutputStream writer = new DataOutputStream (new FileOutputStream (values));
         CsvListReader reader = new CsvListReader (new InputStreamReader (input), preference)) {
      final String[] header = reader.getHeader (true);
      final List<String> columnNames = new ArrayList<> ();
      final List<String> rowNames = new ArrayList<> ();
      for (int i = 1; i < header.length; i++) {
        String columnName = header[i];
        columnNames.add (columnName);
      }

      CellProcessor[] processors = new CellProcessor[header.length];
      fill (processors, new CellProcessorAdaptor () {
        @Override
        public Object execute (Object value, CsvContext context) {
          if ("Inf".equals (value))
            return POSITIVE_INFINITY;
          else if ("-Inf".equals (value))
            return NEGATIVE_INFINITY;
          else if ("NA".equals (value) || "null".equals (value) || value == null)
            return NaN;
          else
            return parseDouble (value.toString ());
        }
      });
      processors[0] = null; // row name column

      for (List<?> row; (row = reader.read (processors)) != null;) {
        rowNames.add ((String) row.get (0));
        for (int i = 1; i < row.size (); i++)
          writer.writeDouble ((Double) row.get (i));
      }

      LinkedHashMap<String, Map<String, Integer>> map = new LinkedHashMap<> ();
      map.put (this.column, invert (columnNames));
      map.put (this.row, invert (rowNames));

      return new DatasetAdapter<String, Double> (datasetName.get (),
                                                 dimensions (new DimensionAdapter<String> (this.column) {

                                                   @Override
                                                   public int size () {
                                                     return columnNames.size ();
                                                   }

                                                   @Override
                                                   public String get (int index) {
                                                     return columnNames.get (index);
                                                   }
                                                 }, new DimensionAdapter<String> (this.row) {

                                                   @Override
                                                   public int size () {
                                                     return rowNames.size ();
                                                   }

                                                   @Override
                                                   public String get (int index) {
                                                     return rowNames.get (index);
                                                   }
                                                 }),
                                                 analyses (),
                                                 new FlatFileStoreValuesAdapter<String> (values, map, true));
    } catch (IOException | RuntimeException e) {
      try {
        values.delete ();
      } catch (Exception ex) {
        log.warn ("Unable to cleanup values file "
                  + values.getAbsolutePath () + " during construction failure because of ", ex);
      }
      throw e;
    }
  }

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object,
   * java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.OutputStream) */
  @Override
  @SneakyThrows (InvalidCoordinateSetException.class)
  public void writeTo (final Dataset<String, Double> dataset,
                       Class<?> type,
                       Type genericType,
                       Annotation[] annotations,
                       MediaType mediaType,
                       MultivaluedMap<String, Object> httpHeaders,
                       OutputStream stream) throws IOException, WebApplicationException {
    try (PrintStream out = new PrintStream (stream)) {
      out.println ("\t" + join (dataset.dimensions ().get (this.column), "\t"));
      for (final String row : dataset.dimensions ().get (this.row))
        out.println (row + "\t" + join (forEach (dataset.values ().get (new AbstractList<Map<String, String>> () {
          private final Dimension<String> columns = dataset.dimensions ().get (COLUMN);

          @Override
          public Map<String, String> get (final int index) {
            return new AbstractMap<String, String> () {
              private final String column = columns.get (index);

              @Override
              public Set<Entry<String, String>> entrySet () {
                return of (entry (COLUMN, column), entry (ROW, row));
              }

              private Entry<String, String> entry (final String key, final String value) {
                return new Entry<String, String> () {

                  @Override
                  public String getKey () {
                    return key;
                  }

                  @Override
                  public String getValue () {
                    return value;
                  }

                  @Override
                  public String setValue (String value) {
                    throw new UnsupportedOperationException ();
                  }
                };
              }
            };
          }

          @Override
          public int size () {
            return columns.size ();
          }
        })).value (), "\t"));
    }
  }

  private static Map<String, Integer> invert (List<String> list) {
    Map<String, Integer> map = new HashMap<> ();
    for (int i = list.size (); --i >= 0; map.put (list.get (i), i));
    return map;
  }
}
