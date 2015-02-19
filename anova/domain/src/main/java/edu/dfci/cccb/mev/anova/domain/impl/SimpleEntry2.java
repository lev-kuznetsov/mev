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
package edu.dfci.cccb.mev.anova.domain.impl;

import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.anova.domain.contract.Anova2.Entry2;

/**
 * @author levk
 * 
 */
@Accessors (fluent = true)
public class SimpleEntry2 implements Entry2 {

  private @Getter @JsonProperty String geneId;
  private @Getter @JsonProperty Double pValue;
  private @Getter @JsonProperty List<Pairing2> changes;
}
