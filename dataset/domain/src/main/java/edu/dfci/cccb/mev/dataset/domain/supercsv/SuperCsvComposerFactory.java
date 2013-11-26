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
package edu.dfci.cccb.mev.dataset.domain.supercsv;

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.ROW;
import static edu.dfci.cccb.mev.dataset.domain.contract.RawInput.TAB_SEPARATED_VALUES;
import static java.lang.String.valueOf;
import static java.util.regex.Pattern.compile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.supercsv.comment.CommentMatcher;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.CsvPreference.Builder;

import edu.dfci.cccb.mev.dataset.domain.contract.Composer;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetComposingException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractComposer;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractComposerFactory;

/**
 * @author levk
 * 
 */
@ToString
@EqualsAndHashCode (callSuper = true)
@Accessors (fluent = true, chain = true)
public class SuperCsvComposerFactory extends AbstractComposerFactory {

  private @Getter @Setter String idColumnName = "id";
  private @Getter @Setter char quoteChar = '"';
  private @Getter @Setter int separatorChar = '\t';
  private @Getter @Setter String endOfLineSymbols = "\n";
  private Collection<Pattern> commentRegExpressions = new ArrayList<> ();

  public SuperCsvComposerFactory commentRegExpressions (String... regExpressions) {
    for (String e : regExpressions)
      commentRegExpressions.add (compile (e));
    return this;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.ComposerFactory#contentType() */
  @Override
  public String contentType () {
    return TAB_SEPARATED_VALUES;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.ComposerFactory#compose(edu.
   * dfci.cccb.mev.dataset.domain.contract.Dataset) */
  @Override
  @SneakyThrows ()
  public Composer compose (final Dataset dataset) throws DatasetComposingException {
    return new AbstractComposer () {

      private CsvPreference preference = new Builder (quoteChar,
                                                      separatorChar,
                                                      endOfLineSymbols).skipComments (new CommentMatcher () {

        @Override
        public boolean isComment (String line) {
          for (Pattern pattern : commentRegExpressions)
            if (pattern.matcher (line).matches ())
              return true;
          return false;
        }
      }).build ();

      @Override
      public void write (final OutputStream out) throws DatasetComposingException, IOException {
        try (CsvListWriter writer = new CsvListWriter (new OutputStreamWriter (out), preference)) {
          List<String> columns = dataset.dimension (COLUMN).keys ();
          List<String> rows = dataset.dimension (ROW).keys ();
          writer.writeHeader (composeHeader (columns));
          for (String row : rows)
            writer.write (composeRow (row, columns));
        } catch (InvalidDimensionTypeException | InvalidCoordinateException e) {
          throw new DatasetComposingException (e);
        }
      }

      private String[] composeHeader (List<String> columns) {
        String[] header = new String[columns.size () + 1];
        header[0] = idColumnName;
        System.arraycopy (columns.toArray (), 0, header, 1, header.length - 1);
        return header;
      }

      private String[] composeRow (String row, List<String> columns) throws InvalidCoordinateException {
        String[] result = new String[columns.size () + 1];
        result[0] = row;
        for (int i = columns.size (); --i >= 0; result[i + 1] = valueOf (dataset.values ().get (row, columns.get (i))));
        return result;
      }
    };
  }
}
