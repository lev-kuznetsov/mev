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
package edu.dfci.cccb.mev.dataset.domain.contract;

import java.io.IOException;
import java.util.List;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;

/**
 * @author levk
 * 
 */
public interface Dataset {

  final String VALID_DATASET_NAME_REGEX = "[a-zA-Z0-9_\\-\\+\\ \\.]+";

  String name ();

  Values values ();

  Dimension dimension (Type type) throws InvalidDimensionTypeException;

  void set (Dimension dimension) throws InvalidDimensionTypeException;

  Analyses analyses ();

  void exportSelection (String name,
                        Type dimension,
                        String selection,
                        Workspace workspace,
                        DatasetBuilder datasetBuilder) throws InvalidDimensionTypeException,
                                                      SelectionNotFoundException,
                                                      InvalidCoordinateException,
                                                      IOException,
                                                      DatasetBuilderException, InvalidDatasetNameException;

  Dataset subset (String name, List<String> columns, List<String> rows);

  Dataset rename (String name) throws InvalidDatasetNameException;
}
