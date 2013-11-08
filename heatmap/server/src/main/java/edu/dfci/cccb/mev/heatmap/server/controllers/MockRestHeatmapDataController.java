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
    result.add (new HeatmapDataEntry (value (), "sample-A", "gene-A", 0, 0));
    result.add (new HeatmapDataEntry (value (), "sample-B", "gene-A", 1, 0));
    result.add (new HeatmapDataEntry (value (), "sample-C", "gene-A", 2, 0));
    result.add (new HeatmapDataEntry (value (), "sample-D", "gene-A", 3, 0));

    result.add (new HeatmapDataEntry (value (), "sample-A", "gene-B", 0, 1));
    result.add (new HeatmapDataEntry (value (), "sample-B", "gene-B", 1, 1));
    result.add (new HeatmapDataEntry (value (), "sample-C", "gene-B", 2, 1));
    result.add (new HeatmapDataEntry (value (), "sample-D", "gene-B", 3, 1));

    result.add (new HeatmapDataEntry (value (), "sample-A", "gene-C", 0, 2));
    result.add (new HeatmapDataEntry (value (), "sample-B", "gene-C", 1, 2));
    result.add (new HeatmapDataEntry (value (), "sample-C", "gene-C", 2, 2));
    result.add (new HeatmapDataEntry (value (), "sample-D", "gene-C", 3, 2));

    result.add (new HeatmapDataEntry (value (), "sample-A", "gene-D", 0, 3));
    result.add (new HeatmapDataEntry (value (), "sample-B", "gene-D", 1, 3));
    result.add (new HeatmapDataEntry (value (), "sample-C", "gene-D", 2, 3));
    result.add (new HeatmapDataEntry (value (), "sample-D", "gene-D", 3, 3));

    result.add (new HeatmapDataEntry (value (), "sample-A", "gene-E", 0, 4));
    result.add (new HeatmapDataEntry (value (), "sample-B", "gene-E", 1, 4));
    result.add (new HeatmapDataEntry (value (), "sample-C", "gene-E", 2, 4));
    result.add (new HeatmapDataEntry (value (), "sample-D", "gene-E", 3, 4));
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
