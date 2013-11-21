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
package edu.dfci.cccb.mev.dataset.domain.prototype;

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.ROW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import edu.dfci.cccb.mev.dataset.domain.contract.Analyses;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.Parser;
import edu.dfci.cccb.mev.dataset.domain.contract.ParserFactory;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilderFactory;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;

/**
 * @author levk
 * 
 */
public abstract class AbstractDatasetBuilder implements DatasetBuilder {

  private @Getter @Setter (onMethod = @_ (@Inject)) Collection<? extends ParserFactory> parserFactories;
  private @Getter @Setter (onMethod = @_ (@Inject)) ValueStoreBuilderFactory valueStoreBuilderFactory;

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder#build(edu.dfci
   * .cccb.mev.dataset.domain.contract.RawInput) */
  @Override
  public Dataset build (RawInput content) throws DatasetBuilderException, InvalidDatasetNameException {
    ValueStoreBuilder valueBuilder = valueStoreBuilderFactory.builder ();
    List<String> rows = new ArrayList<> ();
    List<String> columns = new ArrayList<> ();
    for (Parser parser = parser (content); parser.next ();) {
      valueBuilder.add (parser.value (), parser.dimension (ROW), parser.dimension (COLUMN));
      if (!rows.contains (parser.dimension (ROW)))
        rows.add (parser.dimension (ROW));
      if (!columns.contains (parser.dimension (COLUMN)))
        columns.add (parser.dimension (COLUMN));
    }
    return aggregate (content.name (), valueBuilder.build (), null, // TODO: add
                                                                    // analyses
                      new SimpleDimension (ROW, rows, null, null), // TODO: add
                                                                   // selections
                                                                   // and
                                                                   // annotation
                      new SimpleDimension (COLUMN, columns, null, null)); // TODO:
                                                                          // add
                                                                          // selections
                                                                          // and
                                                                          // annotation
  }

  protected abstract Dataset aggregate (String name,
                                        Values values,
                                        Analyses analyses,
                                        Dimension... dimensions) throws DatasetBuilderException,
                                                                InvalidDatasetNameException;

  protected Parser parser (RawInput content) throws DatasetBuilderException {
    for (ParserFactory parserFactory : parserFactories)
      if (parserFactory.contentType ().equals (content.contentType ()))
        try {
          return parserFactory.parse (content.input ());
        } catch (DatasetBuilderException | IOException e) {
          throw new DatasetBuilderException (e); // TODO: add args
        }
    throw new DatasetBuilderException (); // TODO: add args
  }
}
