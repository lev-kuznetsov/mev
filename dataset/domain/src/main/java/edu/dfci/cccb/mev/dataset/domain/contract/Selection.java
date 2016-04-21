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

import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;

/**
 * @author levk
 * 
 */
@JsonDeserialize (as = SimpleSelection.class)
public interface Selection {

  final String VALID_SELECTION_NAME_REGEX = "[a-zA-Z0-9_\\-\\+\\ \\.]+";

  @JsonProperty
  String name ();

  @JsonProperty(required = false)
  Properties properties ();

  @JsonProperty
  List<String> keys ();
}
