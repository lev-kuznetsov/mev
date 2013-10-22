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
package edu.dfci.cccb.mev.heatmap.controllers;

import static edu.dfci.cccb.mev.heatmap.domain.mock.MockHeatmap.heatmap;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.dfci.cccb.mev.heatmap.domain.Data;
import edu.dfci.cccb.mev.heatmap.domain.mock.MockData;
import edu.dfci.cccb.mev.heatmap.domain.mock.MockHeatmap;

/**
 * @author levk
 * 
 */
public class DataControllerTest {

  private DataController controller = new DataController ();

  @Test
  public void data () throws Exception {
    MockHeatmap h = heatmap ("mock");
    Data d = new MockData ();
    h.data (d);
    assertEquals (d, controller.data (h, null));
  }
}
