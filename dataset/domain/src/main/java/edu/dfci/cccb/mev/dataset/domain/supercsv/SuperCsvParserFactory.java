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

import static edu.dfci.cccb.mev.dataset.domain.contract.RawInput.TAB_SEPARATED_VALUES;
import static java.util.regex.Pattern.compile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;
import org.supercsv.comment.CommentMatcher;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference.Builder;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractParserFactory;

/**
 * @author levk
 * 
 */
@NoArgsConstructor
public class SuperCsvParserFactory extends AbstractParserFactory {

  private char quoteChar = '"';
  private int separatorChar = '\t';
  private String endOfLineSymbols = "\n";
  private Collection<Pattern> commentRegExpressions = new ArrayList<> ();
  private SuperCsvParser.RowIdParser rowIdParser;
  {
    commentRegExpressions ("[\t ]+#[.]+", "[\\! ].+", "[\\^ ].+","[#].+");
  }
  public SuperCsvParserFactory(SuperCsvParser.RowIdParser rowIdParser){
    this.rowIdParser=rowIdParser;
  }
  public SuperCsvParserFactory quoteChar (char quoteChar) {
    this.quoteChar = quoteChar;
    return this;
  }
  @Override
  public void addCommentRegExpression(String regex){
    this.commentRegExpressions.add (compile (regex));
  }
  public SuperCsvParserFactory separatorChar (int separatorChar) {
    this.separatorChar = separatorChar;
    return this;
  }

  public SuperCsvParserFactory endOfLineSymbols (String endOfLineSymbols) {
    this.endOfLineSymbols = endOfLineSymbols;
    return this;
  }

  public SuperCsvParserFactory commentRegExpressions (String... commentRegExpressions) {
    for (String regex : commentRegExpressions)
      this.commentRegExpressions.add (compile (regex));
    return this;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.builders.ParserFactory#contentType
   * () */
  @Override
  public String contentType () {
    return TAB_SEPARATED_VALUES;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.builders.ParserFactory#parse
   * (java.io.InputStream) */
  @Override
  public SuperCsvParser parse (InputStream input) throws DatasetBuilderException {
    return new SuperCsvParser (new CsvListReader (new InputStreamReader (input),
                                                  new Builder (quoteChar,
                                                               separatorChar,
                                                               endOfLineSymbols).skipComments (new CommentMatcher () {

                                                    @Override
                                                    public boolean isComment (String line) {
                                                      for (Pattern pattern : commentRegExpressions)
                                                        if (pattern.matcher (line).matches ())
                                                          return true;
                                                      return false;
                                                    }
                                                  }).build ()), rowIdParser);
  }
}
