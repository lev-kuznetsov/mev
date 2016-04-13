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

import static java.util.regex.Pattern.compile;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Accessors;
import ch.lambdaj.Lambda;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.subset.DataSubset;
import edu.dfci.cccb.mev.io.implementation.TemporaryFile;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode
@ToString
@Accessors (fluent = true, chain = true)
public abstract class AbstractDataset implements Dataset, AutoCloseable {

  private static final Pattern VALID_DATASET_NAME_PATTERN = compile (VALID_DATASET_NAME_REGEX);

  private @Getter String name;

  protected AbstractDataset (String name) throws InvalidDatasetNameException {
    rename (name);
  }

  public Dataset rename (String name) throws InvalidDatasetNameException {
    if (!VALID_DATASET_NAME_PATTERN.matcher (name).matches ())
      throw new InvalidDatasetNameException ().name (name);
    this.name = name;
    return this;
  }

  @Override
  public void exportSelection (String name,
                               Type dimension,
                               String selection,
                               Workspace workspace,
                               DatasetBuilder builder) throws InvalidDimensionTypeException,
                                                      SelectionNotFoundException,
                                                      InvalidCoordinateException,
                                                      IOException,
                                                      DatasetBuilderException,
                                                      InvalidDatasetNameException {
    List<String> columns = dimension == Type.COLUMN
                                                   ? dimension (Type.COLUMN).selections ().get (selection).keys ()
                                                   : dimension (Type.COLUMN).keys ();
    List<String> rows = dimension == Type.ROW ? dimension (Type.ROW).selections ().get (selection).keys () :
                                             dimension (Type.ROW).keys ();
    try (TemporaryFile temp = new TemporaryFile ()) {
      try (PrintStream out = new PrintStream (temp)) {
        out.println ("\t" + Lambda.join (columns, "\t"));
        for (String row : rows) {
          out.print (row);
          for (String column : columns) {
            out.print ("\t");
            out.print (values ().get (row, column));
          }
          out.println ();
        }
      }

      workspace.put (builder.build (new MockTsvInput (name, temp)));
    }
  }

  @Override
  @SneakyThrows (InvalidDatasetNameException.class)
  public Dataset subset (String name, List<String> columns, List<String> rows) {
    return new DataSubset (name, this, columns, rows);
  }
}
