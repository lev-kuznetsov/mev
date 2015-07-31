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
package edu.dfci.cccb.mev.nmf.domain;

import lombok.ToString;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;

/**
 * @author levk
 * 
 */
@ToString
public class Nmf extends AbstractAnalysis<Nmf> {
  private final @JsonProperty String type = "Non-Negative Matrix Factorization";
  private @Getter @JsonProperty Dataset w;
  private @Getter @JsonProperty H h;

  @Override
  @JsonProperty
  public String name () {
    return super.name ();
  }

  @ToString
  public static class H {
    private @Getter @JsonProperty Dataset matrix;
    private @Getter @JsonProperty Node root;
  }
}
