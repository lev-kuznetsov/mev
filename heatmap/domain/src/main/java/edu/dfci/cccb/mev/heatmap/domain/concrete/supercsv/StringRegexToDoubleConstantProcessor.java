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
package edu.dfci.cccb.mev.heatmap.domain.concrete.supercsv;

import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.util.CsvContext;

/**
 * @author levk
 * 
 */
public abstract class StringRegexToDoubleConstantProcessor extends CellProcessorAdaptor implements DoubleCellProcessor {

  private final Pattern[] patterns;
  private final double constant;

  /**
   * @param next
   */
  public StringRegexToDoubleConstantProcessor (CellProcessor next, double constant, Pattern... patterns) {
    super (next);
    this.patterns = patterns;
    this.constant = constant;
  }

  public StringRegexToDoubleConstantProcessor (CellProcessor next, double constant, String... patterns) {
    super (next);
    this.constant = constant;
    this.patterns = new Pattern[patterns.length];
    for (int index = patterns.length; --index >= 0; this.patterns[index] = compile (patterns[index]));
  }

  /* (non-Javadoc)
   * @see
   * org.supercsv.cellprocessor.ift.CellProcessor#execute(java.lang.Object,
   * org.supercsv.util.CsvContext) */
  @Override
  public Object execute (Object value, CsvContext context) {
    if (value instanceof String)
      for (Pattern pattern : patterns)
        if (pattern.matcher ((String) value).matches ())
          return constant;
    return next.execute (value, context);
  }
}
