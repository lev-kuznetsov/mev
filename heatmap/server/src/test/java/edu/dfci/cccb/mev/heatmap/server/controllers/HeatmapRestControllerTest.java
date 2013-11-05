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

import org.junit.Test;

import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapNotFoundException;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;
import edu.dfci.cccb.mev.heatmap.domain.concrete.ListWorkspace;
import edu.dfci.cccb.mev.test.mock.MockHeatmap;

import static org.junit.Assert.*;

/**
 * @author levk
 * 
 */
public class HeatmapRestControllerTest {

  private HeatmapRestController rest;
  private Workspace workspace;

  {
    workspace = new ListWorkspace ();
    rest = new HeatmapRestController (workspace);
  }

  @Test
  public void put () throws Exception {
    Heatmap mock = new MockHeatmap ("mock");
    rest.put ("hello", mock);
    assertEquals ("hello", rest.get (mock).name ());
  }

  @Test
  public void get () throws Exception {
    Heatmap mock = new MockHeatmap ("mock");
    rest.put ("hello", mock);
    assertEquals ("hello", rest.get (mock).name ());
  }
  
  @Test (expected = HeatmapNotFoundException.class)
  public void removeNonExisting () throws Exception {
    Heatmap mock = new MockHeatmap ("mock");
    rest.put ("hello", mock);
    assertEquals ("hello", rest.get (mock).name ());
    rest.remove (new MockHeatmap ("mock2"));
    fail ();
  }
}
