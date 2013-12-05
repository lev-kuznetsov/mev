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

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.hcl.domain.contract.Branch;
import edu.dfci.cccb.mev.hcl.domain.contract.HclResult;
import edu.dfci.cccb.mev.hcl.domain.contract.Leaf;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;

/**
 * @author levk
 * 
 */
@ToString
@EqualsAndHashCode (callSuper = true)
@Accessors (fluent = true, chain = true)
public abstract class AbstractHclResult extends AbstractAnalysis<AbstractHclResult> implements HclResult {

  private @Getter @Setter Node root;
  private @Getter @Setter Dimension dimension;

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.hcl.domain.contract.HclResult#apply() */
  @Override
  public List<String> apply () throws DatasetException {
    dimension.reorder (traverse (root, new ArrayList<String> ()));
    return dimension.keys ();
  }

  private List<String> traverse (Node node, List<String> accumulator) {
    if (node instanceof Branch) {
      for (Node child : ((Branch) node).children ())
        traverse (child, accumulator);
    } else
      accumulator.add (((Leaf) node).name ());
    return accumulator;
  }
}
