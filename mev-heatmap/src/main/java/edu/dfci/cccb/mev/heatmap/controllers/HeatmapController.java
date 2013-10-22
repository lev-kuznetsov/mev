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

import static edu.dfci.cccb.mev.heatmap.domain.Heatmap.HEATMAP_NAME_PATTERN_EXPRESSION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.dfci.cccb.mev.heatmap.domain.Folder;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;

/**
 * @author levk
 * 
 */
@Controller
@RequestMapping ("/heatmap")
public class HeatmapController {

  private Workspace workspace;

  @Autowired
  public HeatmapController (Workspace workspace) {
    this.workspace = workspace;
  }

  @RequestMapping (method = GET)
  @ResponseBody
  public List<? extends Folder> list () {
    return workspace.list ();
  }

  @RequestMapping (value = "/{id:" + HEATMAP_NAME_PATTERN_EXPRESSION + "}", method = PUT)
  @ResponseStatus (OK)
  public void put (@PathVariable ("id") String name, @RequestParam ("filedata") Heatmap heatmap) {
    heatmap.rename (name);
    workspace.put (heatmap);
  }

  @RequestMapping (method = POST)
  @ResponseBody
  public String add (@RequestParam ("filedata") Heatmap heatmap) {
    return workspace.add (heatmap);
  }

  @RequestMapping (value = "/view", method = GET)
  public String view () {
    return "heatmap";
  }
}
