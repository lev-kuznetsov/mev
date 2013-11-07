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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;
import edu.dfci.cccb.mev.heatmap.domain.concrete.ListWorkspace;
import edu.dfci.cccb.mev.test.mock.MockHeatmap;

/**
 * @author levk
 * 
 */
public class WorkspaceRestControllerTest {

  private Workspace workspace;
  private WorkspaceRestController controller;

  {
    workspace = new ListWorkspace ();
    controller = new WorkspaceRestController (workspace);
  }

  @Test
  public void list () {
    assertEquals (workspace.list (), controller.list ());
  }

  @Test
  public void add () {
    Heatmap heatmap = new MockHeatmap ("mock");
    controller.add (heatmap);
    assertEquals (workspace.list ().get (0), heatmap.name ());
  }
}
