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
package edu.dfci.cccb.mev.limma.domain.simple;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.limma.domain.contract.Limma.Entry;

/**
 * @author levk
 * 
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Accessors (fluent = true)
public class SimpleEntry implements Entry {

  private @Getter @JsonProperty String id;
  private @Getter @JsonProperty double logFoldChange;
  private @Getter @JsonProperty double averageExpression;
  private @Getter @JsonProperty double pValue;
  private @Getter @JsonProperty double qValue;
  private @Getter @JsonProperty double t;
  private @Getter @JsonProperty double logPValue;
}
