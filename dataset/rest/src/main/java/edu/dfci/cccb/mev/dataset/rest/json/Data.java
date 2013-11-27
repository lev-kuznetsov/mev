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
package edu.dfci.cccb.mev.dataset.rest.json;

import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author levk
 * 
 */
@AllArgsConstructor
public class Data {

  private @JsonView List<String> row;
  private @JsonView List<String> column;
  private @JsonView Collection<Value> values;
}
