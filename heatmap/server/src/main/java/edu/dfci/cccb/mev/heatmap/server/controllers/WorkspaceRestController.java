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

import static edu.dfci.cccb.mev.heatmap.domain.Heatmap.HEATMAP_VALID_NAME_PATTERN_EXPRESSION;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.inject.Inject;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapNotFoundException;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/heatmap")
@RequiredArgsConstructor (onConstructor = @_ (@Inject))
@ToString
@EqualsAndHashCode
public class WorkspaceRestController {

  private final Workspace workspace;

  @RequestMapping (value = { "", "/" }, method = GET)
  public List<String> list () {
    return workspace.list ();
  }

  @RequestMapping (value = { "", "/" }, method = POST)
  public String add (@RequestParam ("matrix") Heatmap heatmap) {
    workspace.put (heatmap);
    return heatmap.name ();
  }

  @RequestMapping (value = "/{id:" + HEATMAP_VALID_NAME_PATTERN_EXPRESSION + "}", method = PUT)
  public void put (@PathVariable ("id") String id,
                   @RequestParam ("matrix") Heatmap heatmap) {
    heatmap.rename (id);
    workspace.put (heatmap);
  }

  @RequestMapping (value = "/{id:" + HEATMAP_VALID_NAME_PATTERN_EXPRESSION + "}", method = DELETE)
  public void remove (@PathVariable ("id") Heatmap heatmap) throws HeatmapNotFoundException {
    workspace.remove (heatmap.name ());
  }
}
