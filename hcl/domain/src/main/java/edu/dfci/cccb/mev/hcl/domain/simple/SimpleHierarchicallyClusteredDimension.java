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
package edu.dfci.cccb.mev.hcl.domain.simple;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Annotation;
import edu.dfci.cccb.mev.dataset.domain.contract.Selections;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDimension;
import edu.dfci.cccb.mev.hcl.domain.contract.Branch;
import edu.dfci.cccb.mev.hcl.domain.contract.Leaf;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;

/**
 * @author levk
 * 
 */
@ToString
@EqualsAndHashCode (callSuper = true)
@Accessors (fluent = true)
public class SimpleHierarchicallyClusteredDimension extends AbstractDimension {

  private @Getter Node root;
  private @Getter List<String> keys;

  /**
   * @param type
   */
  public SimpleHierarchicallyClusteredDimension (Type type, Node root, Selections selections, Annotation annotation) {
    super (type);
    selections (selections);
    annotation (annotation);
    this.root = root;
    keys = unmodifiableList (traverse (root, new ArrayList<String> ()));
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
