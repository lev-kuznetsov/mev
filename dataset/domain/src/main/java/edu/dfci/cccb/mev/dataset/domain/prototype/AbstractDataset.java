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

import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode
@ToString
@Accessors (fluent = true)
public abstract class AbstractDataset implements Dataset {

  private static final Pattern VALID_DATASET_NAME_PATTERN = compile (VALID_DATASET_NAME_REGEX);

  private @Getter String name;

  protected AbstractDataset (String name) throws InvalidDatasetNameException {
    if (!VALID_DATASET_NAME_PATTERN.matcher (name).matches ())
      throw new InvalidDatasetNameException ().name (name);
    this.name = name;
  }
}
