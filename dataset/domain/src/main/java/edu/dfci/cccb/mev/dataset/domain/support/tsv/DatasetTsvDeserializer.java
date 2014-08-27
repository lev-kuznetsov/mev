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

package edu.dfci.cccb.mev.dataset.domain.support.tsv;

import static edu.dfci.cccb.mev.dataset.domain.prototype.AnalysisAdapter.analyses;
import static edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter.dimensions;
import static java.io.File.createTempFile;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.parseDouble;
import static java.lang.String.valueOf;
import static java.util.Arrays.fill;
import static java.util.regex.Pattern.compile;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import lombok.extern.log4j.Log4j;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.comment.CommentMatcher;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.CsvPreference.Builder;
import org.supercsv.util.CsvContext;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter;
import edu.dfci.cccb.mev.dataset.domain.support.binary.FlatFileStoreValuesAdapter;

/**
 * @author levk
 * @since ASHA
 */
@Log4j
public class DatasetTsvDeserializer {

  /**
   * Row dimension name
   */
  public static final String ROW = "row";
  /**
   * Column dimension name
   */
  public static final String COLUMN = "column";

  /**
   * Comment regexes
   */
  private static final String[] COMMENT_EXPRESSIONS = new String[] { "[\t ]*#[^$]*" };

  /**
   * End of line symbols
   */
  private static final char[] END_OF_LINE_SYMBOLS = "\n\r".toCharArray ();

  /**
   * Delimiter character
   */
  private static final int DELIMITER_CHAR = '\t';

  /**
   * Quote character
   */
  private static final char QUOTE_CHAR = '"';

  /**
   * Deserializes a dataset
   * 
   * @param name
   * @param input
   * @return
   * @throws IOException
   */
  public Dataset<String, Double> deserialize (String name, InputStream input) throws IOException {
    File values = createTempFile ("mev.", ".data");

    try (DataOutputStream writer = new DataOutputStream (new FileOutputStream (values))) {
      CsvPreference configuration = new Builder (QUOTE_CHAR,
                                                 DELIMITER_CHAR,
                                                 valueOf (END_OF_LINE_SYMBOLS)).skipComments (new CommentMatcher () {
        private final Pattern[] COMMENTS;

        {
          COMMENTS = new Pattern[COMMENT_EXPRESSIONS.length];
          for (int i = COMMENTS.length; --i >= 0; COMMENTS[i] = compile (COMMENT_EXPRESSIONS[i]));
        }

        @Override
        public boolean isComment (String line) {
          for (Pattern comment : COMMENTS)
            if (comment.matcher (line).matches ())
              return true;
          return false;
        }
      }).build ();

      try (CsvListReader reader = new CsvListReader (new InputStreamReader (input),
                                                     configuration)) {
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
        map.put (COLUMN, invert (columnNames));
        map.put (ROW, invert (rowNames));

        return new DatasetAdapter<String, Double> (name, dimensions (new DimensionAdapter<String> (COLUMN) {

          @Override
          public int size () {
            return columnNames.size ();
          }

          @Override
          public String get (int index) {
            return columnNames.get (index);
          }
        }, new DimensionAdapter<String> (ROW) {

          @Override
          public int size () {
            return rowNames.size ();
          }

          @Override
          public String get (int index) {
            return rowNames.get (index);
          }
        }), analyses (), new FlatFileStoreValuesAdapter<String> (values, map, true));
      }
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

  private static Map<String, Integer> invert (List<String> list) {
    Map<String, Integer> map = new HashMap<> ();
    for (int i = list.size (); --i >= 0; map.put (list.get (i), i));
    return map;
  }
}
