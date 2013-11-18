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

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.heatmap.domain.Data;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author levk
 *
 */
@RestController
@RequestMapping ("/heatmap/rest/{heatmap:" + HEATMAP_VALID_NAME_PATTERN_EXPRESSION + "}/data")
public class DataRestController {

  @RequestMapping (method = GET)
  public Data get (@PathVariable ("heatmap") Heatmap heatmap) {
    // TODO: stub
    return null;
  }
}
