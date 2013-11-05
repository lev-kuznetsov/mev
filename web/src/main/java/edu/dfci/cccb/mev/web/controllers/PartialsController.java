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
package edu.dfci.cccb.mev.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author levk
 * 
 */
@Controller
@ToString
@RequestMapping ("/container/view")
@Log4j
public class PartialsController {

  @RequestMapping (value = "elements/{view}", method = GET)
  public String elements (@PathVariable ("view") String view) {
    log.debug ("Getting elements view " + view);
    return "elements/" + view;
  }

  @RequestMapping (value = "partials/{view}", method = GET)
  public String partials (@PathVariable ("view") String view) {
    return "partials/" + view;
  }
}
