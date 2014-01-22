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
import static org.springframework.web.context.request.RequestAttributes.*;

import javax.faces.bean.ViewScoped;

import lombok.ToString;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

/**
 * @author levk
 * 
 */
@Controller
@ToString
@RequestMapping ("/container/view")
public class ViewController {
  
  @RequestMapping (value = "templates/**", method = GET)
  public void templates (NativeWebRequest request) {}
  
  @RequestMapping (value = "elements/**", method = GET)
  public void elements () {}

  @RequestMapping (value = "partials/{view}", method = GET)
  public String partials (@PathVariable ("view") String view) {
    return "partials/" + view;
  }
}
