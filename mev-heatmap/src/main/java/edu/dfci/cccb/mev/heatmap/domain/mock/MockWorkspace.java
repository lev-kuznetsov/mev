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
package edu.dfci.cccb.mev.heatmap.domain.mock;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.dfci.cccb.mev.heatmap.domain.Folder;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapNotFoundException;
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractWorkspace;

/**
 * @author levk
 * 
 */
public class MockWorkspace extends AbstractWorkspace {

  private final Map<String, Heatmap> maps = new HashMap<> ();

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Workspace#get(java.lang.String) */
  @Override
  public Heatmap get (String id) throws HeatmapNotFoundException {
    if (maps.containsKey (id))
      return maps.get (id);
    else
      throw new HeatmapNotFoundException ();
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.heatmap.domain.Workspace#put(edu.dfci.cccb.mev.heatmap
   * .domain.Heatmap) */
  @Override
  public void put (Heatmap heatmap) {
    maps.put (heatmap.name (), heatmap);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Workspace#add(edu.dfci.cccb.mev.heatmap.domain.Heatmap)
   */
  @Override
  public String add (Heatmap heatmap) {
    String name = heatmap.name ();
    if (maps.containsKey (heatmap.name ()))
      for (int index = 1; maps.containsKey (name = heatmap.name () + "-" + index); index++);
    heatmap.rename (name);
    put (heatmap);
    return name;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Workspace#list() */
  @Override
  public List<? extends Folder> list () {
    return asList (new Folder () {
      /* (non-Javadoc)
       * @see edu.dfci.cccb.mev.heatmap.domain.Folder#list() */
      @Override
      public List<String> list () {
        return asList (maps.keySet ().toArray (new String[0]));
      }

      /* (non-Javadoc)
       * @see edu.dfci.cccb.mev.heatmap.domain.Folder#name() */
      @Override
      public String name () {
        return "mock";
      }
    });
  }

  public static MockWorkspace workspace (final Heatmap... heatmaps) {
    return new MockWorkspace () {
      {
        for (Heatmap heatmap : heatmaps)
          put (heatmap);
      }
    };
  }
}
