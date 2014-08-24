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

import static com.mycila.inject.internal.guava.collect.ImmutableMap.of;
import static java.util.regex.Pattern.compile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import lombok.SneakyThrows;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.comment.CommentMatcher;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.CsvPreference.Builder;
import org.supercsv.util.CsvContext;

import edu.dfci.cccb.mev.dataset.domain.Values.Value;
import edu.dfci.cccb.mev.dataset.domain.support.Consumer;
import edu.dfci.cccb.mev.dataset.domain.support.Parser;
import edu.dfci.cccb.mev.dataset.domain.support.Resolver;

/**
 * Tab separated value parser
 * 
 * @author levk
 * @since CRYSTAL
 */
public class TsvParser implements Parser<String> {

  /**
   * Row dimension name
   */
  public static final String ROW = "row";
  /**
   * Column dimension name
   */
  public static final String COLUMN = "column";

  /**
   * 
   * @param input
   * @param valueListener
   * @param keyListeners
   * @throws IOException
   */
  public void parse (InputStream input,
                     Consumer<Value<String, Double>> valueListener,
                     Consumer<String>[] keyListeners) throws IOException {
    parse (input, valueListener, keyListeners, STRING, DOUBLE);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.support.parser.Parser#parse(java.io.
   * InputStream, edu.dfci.cccb.mev.dataset.domain.support.parser.Consumer,
   * edu.dfci.cccb.mev.dataset.domain.support.parser.Consumer[],
   * edu.dfci.cccb.mev.dataset.domain.support.parser.Resolver,
   * edu.dfci.cccb.mev.dataset.domain.support.parser.Resolver) */
  @Override
  public <K, V> void parse (InputStream input,
                            Consumer<Value<K, V>> valueListener,
                            Consumer<K>[] keyListeners,
                            Resolver<String, K> keyResolver,
                            Resolver<String, V> valueResolver) throws IOException {
    parse (input, valueListener, keyListeners, keyResolver, valueResolver, '"', '\t', "\n", "[\t ]+#[.]+");
  }

  /**
   * @param input
   * @param valueListener
   * @param keyListeners
   * @param keyResolver
   * @param valueResolver
   * @param quoteChar
   * @param delimiterChar
   * @param endOfLineSymbols
   * @param commentRegexes
   * @throws IOException
   */
  public <K, V> void parse (InputStream input,
                            Consumer<Value<K, V>> valueListener,
                            Consumer<K>[] keyListeners,
                            final Resolver<String, K> keyResolver,
                            final Resolver<String, V> valueResolver,
                            char quoteChar,
                            int delimiterChar,
                            String endOfLineSymbols,
                            final String... commentRegexes) throws IOException {
    CsvPreference configuration = new Builder (quoteChar,
                                               delimiterChar,
                                               endOfLineSymbols).skipComments (new CommentMatcher () {
      private final Pattern[] COMMENTS;

      {
        COMMENTS = new Pattern[commentRegexes.length];
        for (int i = COMMENTS.length; --i >= 0; COMMENTS[i] = compile (commentRegexes[i]));
      }

      @Override
      public boolean isComment (String line) {
        for (Pattern comment : COMMENTS)
          if (comment.matcher (line).matches ())
            return true;
        return false;
      }
    }).build ();

    try (CsvListReader reader1 = new CsvListReader (new InputStreamReader (input),
                                                    configuration)) {
      final CsvListReader reader = reader1; // https://github.com/cobertura/cobertura/issues/87
      final String[] header = reader.getHeader (true);
      final List<K> columnNames = new ArrayList<> ();
      columnNames.add (null);
      for (int i = 1; i < header.length; i++) {
        K columnName = keyResolver.resolve (header[i]);
        columnNames.add (columnName);
        keyListeners[1].consume (columnName);
      }

      CellProcessor[] processors = new CellProcessor[header.length];
      processors[0] = new CellProcessorAdaptor () {

        @Override
        @SneakyThrows (IOException.class)
        public Object execute (Object value, CsvContext context) {
          return keyResolver.resolve ((String) value);
        }
      };

      for (int i = processors.length; --i > 0; processors[i] = new CellProcessorAdaptor () {

        @Override
        @SneakyThrows (IOException.class)
        public Object execute (Object value, CsvContext context) {
          return valueResolver.resolve ((String) value);
        }
      });

      for (List<?> row; (row = reader.read (processors)) != null;) {
        @SuppressWarnings ("unchecked") K rowName = (K) row.get (0);
        keyListeners[0].consume (rowName);
        for (int i = 1; i < row.size (); i++) {
          @SuppressWarnings ("unchecked") V value = (V) row.get (i);
          valueListener.consume (new Value<K, V> (value, of (COLUMN, columnNames.get (i), ROW, rowName)));
        }
      }
    }
  }
}
