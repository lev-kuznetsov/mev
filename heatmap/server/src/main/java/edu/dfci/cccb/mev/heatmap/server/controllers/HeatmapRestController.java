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
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapNotFoundException;
import edu.dfci.cccb.mev.heatmap.domain.InvalidHeatmapNameException;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/heatmap/rest/{heatmap:" + HEATMAP_VALID_NAME_PATTERN_EXPRESSION + "}")
@ToString
@RequiredArgsConstructor (onConstructor = @_ (@Inject))
public class HeatmapRestController {

  private final Workspace workspace;

  @RequestMapping (method = GET)
  public Heatmap get (@PathVariable ("heatmap") Heatmap heatmap) {
    return heatmap;
  }

  @RequestMapping (method = PUT)
  public void put (@PathVariable ("heatmap") String id,
                   @RequestParam ("matrix") Heatmap heatmap) throws InvalidHeatmapNameException {
    heatmap.rename (id);
    workspace.put (heatmap);
  }

  @RequestMapping (method = DELETE)
  public void remove (@PathVariable ("heatmap") Heatmap heatmap) throws HeatmapNotFoundException {
    workspace.remove (heatmap.name ());
  }
}
