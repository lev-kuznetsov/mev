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

import javax.inject.Inject;

import lombok.ToString;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.dfci.cccb.mev.web.domain.reflection.Reflector;

/**
 * @author levk
 * 
 */
@Controller
@ToString
@RequestMapping ("/api")
public class ReflectionController {

  private final Reflector reflection;

  @Inject
  public ReflectionController (Reflector reflection) {
    this.reflection = reflection;
  }

  @RequestMapping (method = GET)
  public String api (Model model) {
    model.addAttribute ("reflection", reflection);
    return "api";
  }
}
