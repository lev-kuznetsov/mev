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
package edu.dfci.cccb.mev.heatmap.server.controllers;

import static java.util.UUID.randomUUID;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.heatmap.server.mock.HeatmapDataEntry;
import edu.dfci.cccb.mev.heatmap.server.mock.HeatmapHierarchicalClusteringInternalNode;
import edu.dfci.cccb.mev.heatmap.server.mock.HeatmapHierarchicalClusteringLeaf;
import edu.dfci.cccb.mev.heatmap.server.mock.HeatmapHierarchicalClusteringNode;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/heatmap/mock")
public class MockRestHeatmapDataController {

  private Random random = new Random ();

  private double value () {
    return random.nextDouble () - 0.5;
  }

  @RequestMapping (value = "/data", method = GET)
  public Collection<HeatmapDataEntry> data () {
    Collection<HeatmapDataEntry> result = new ArrayList<> ();

    int rows = random.nextInt (100) + 100;
    int columns = random.nextInt (100) + 100;

    List<String> rowKeys = new ArrayList<> (), columnKeys = new ArrayList<> ();
    for (int row = rows; --row >= 0; rowKeys.add ("ROW-" + randomUUID ()));
    for (int column = columns; --column >= 0; columnKeys.add ("COLUMN-" + randomUUID ()));

    for (int row = rows; --row >= 0;)
      for (int column = columns; --column >= 0; result.add (new HeatmapDataEntry ((random.nextDouble () - 0.5) * 6,
                                                                                  columnKeys.get (column),
                                                                                  rowKeys.get (row),
                                                                                  column,
                                                                                  row)));
    return result;
  }

  @RequestMapping (value = "/cluster", method = GET)
  public HeatmapHierarchicalClusteringNode cluster () {
    double distance = value ();
    HeatmapHierarchicalClusteringNode left = null, right = null;
    if (distance >= 0) {
      left = cluster ();
      right = cluster ();
    } else {
      left = new HeatmapHierarchicalClusteringLeaf (distance, randomUUID ().toString ());
      right = new HeatmapHierarchicalClusteringLeaf (-distance, randomUUID ().toString ());
    }
    return new HeatmapHierarchicalClusteringInternalNode (distance, left, right);
  }
}
