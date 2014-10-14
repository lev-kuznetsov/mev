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
package edu.dfci.cccb.mev.stats.domain.contract;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;

/**
 * @author levk
 * 
 */
public interface Fisher extends Analysis {
  @JsonProperty
  double oddsRatio ();

  @JsonProperty
  double pValue ();
  
  @JsonProperty
  String type ();

  @JsonProperty
  String name ();
  
  @JsonProperty
  Calendar timestamp ();
}
