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
import static edu.dfci.cccb.mev.heatmap.support.AreaMethodArgumentResolver.AREA_STRING_REPRESENTATION_PATTERN_EXPRESSION;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import lombok.extern.log4j.Log4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.heatmap.domain.Area;
import edu.dfci.cccb.mev.heatmap.domain.Data;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.InvalidDataRequestException;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/heatmap/{id:" + HEATMAP_NAME_PATTERN_EXPRESSION + "}/data")
@Log4j
public class DataController {

  @RequestMapping (value = "/{area:" + AREA_STRING_REPRESENTATION_PATTERN_EXPRESSION + "}", method = GET)
  public Data data (@PathVariable ("id") Heatmap heatmap,
                    @PathVariable ("area") Area area) throws InvalidDataRequestException {
    if (log.isDebugEnabled ())
      log.debug ("Getting data for heatmap " + heatmap + " for area " + area);
    return heatmap.data (area);
  }
}
