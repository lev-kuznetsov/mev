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
package edu.dfci.cccb.mev.heatmap.domain.concrete;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;

import edu.dfci.cccb.mev.heatmap.domain.HclInternalNode;
import edu.dfci.cccb.mev.heatmap.domain.HclLeaf;
import edu.dfci.cccb.mev.heatmap.domain.HclNode;

/**
 * @author levk
 * 
 */
public abstract class SimpleHclNode implements HclNode {

  private double distance;

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.HclNode#distance() */
  @Override
  public double distance () {
    return distance;
  }

  public void set (double distance) {
    this.distance = distance;
  }

  public static class SimpleHclInternalNode extends SimpleHclNode implements HclInternalNode {

    private Collection<HclNode> children;

    public SimpleHclInternalNode (HclNode... children) {
      this.children = new ArrayList<> (asList (children));
    }

    /* (non-Javadoc)
     * @see edu.dfci.cccb.mev.heatmap.domain.HclInternalNode#children() */
    @Override
    public Collection<HclNode> children () {
      return children;
    }
  }

  public static class SimpleHclLeaf extends SimpleHclNode implements HclLeaf {
    private String id;

    /**
     * 
     */
    public SimpleHclLeaf (String id) {
      this.id = id;
    }

    /* (non-Javadoc)
     * @see edu.dfci.cccb.mev.heatmap.domain.HclLeaf#id() */
    @Override
    public String id () {
      return id;
    }
  }
}
