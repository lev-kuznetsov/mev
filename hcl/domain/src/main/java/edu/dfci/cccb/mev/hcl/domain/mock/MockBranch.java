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
package edu.dfci.cccb.mev.hcl.domain.mock;

import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Set;

import edu.dfci.cccb.mev.hcl.domain.contract.Node;
import edu.dfci.cccb.mev.hcl.domain.prototype.AbstractBranch;

/**
 * @author levk
 * 
 */
public class MockBranch extends AbstractBranch<Double> {

  private Set<Node<Double>> children;
  private double distance;

  public MockBranch (double distance, Set<Node<Double>> children) {
    this.distance = distance;
    this.children = unmodifiableSet (new HashSet<> (children));
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.hcl.domain.contract.Branch#children() */
  @Override
  public Set<Node<Double>> children () {
    return children;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.hcl.domain.contract.Node#distance() */
  @Override
  public Double distance () {
    return distance;
  }
}
