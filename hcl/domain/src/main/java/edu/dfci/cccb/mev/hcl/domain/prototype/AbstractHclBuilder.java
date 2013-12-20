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
package edu.dfci.cccb.mev.hcl.domain.prototype;

import static lombok.AccessLevel.PROTECTED;

import javax.inject.Inject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysisBuilder;
import edu.dfci.cccb.mev.hcl.domain.contract.Hcl;
import edu.dfci.cccb.mev.hcl.domain.contract.HclBuilder;
import edu.dfci.cccb.mev.hcl.domain.contract.Linkage;
import edu.dfci.cccb.mev.hcl.domain.contract.Metric;
import edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder;

/**
 * @author levk
 * 
 */
@ToString
@EqualsAndHashCode (callSuper = true)
@Accessors (fluent = true, chain = true)
public abstract class AbstractHclBuilder extends AbstractAnalysisBuilder<HclBuilder, Hcl> implements HclBuilder {

  private @Getter (PROTECTED) @Setter (onMethod = @_ (@Inject)) Linkage linkage;
  private @Getter (PROTECTED) @Setter (onMethod = @_ (@Inject)) Metric metric;
  private @Getter (PROTECTED) @Setter (onMethod = @_ (@Inject)) Dimension dimension;
  private @Getter (PROTECTED) @Setter (onMethod = @_ (@Inject)) NodeBuilder nodeBuilder;

  protected AbstractHclBuilder () {
    super ("Hierarchical Clustering");
  }
}
