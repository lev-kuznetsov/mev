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
package edu.dfci.cccb.mev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.dfci.cccb.mev.domain.ClientConfiguration;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author levk
 * 
 */
@Controller
@RequestMapping ("/preferences")
public class ClientConfigurationController {

  private @Autowired ClientConfiguration configuration;

  @RequestMapping (value = "/{key}", method = GET)
  @ResponseBody
  public String get (@PathVariable ("key") String key) throws ClientConfigurationKeyNotFoundException {
    String result = configuration.get (key);
    if (result == null)
      throw new ClientConfigurationKeyNotFoundException (key);
    else
      return result;
  }

  @RequestMapping (value = "/key", method = PUT)
  @ResponseStatus (OK)
  public void put (@PathVariable ("key") String key, @RequestParam ("value") String value) {
    configuration.put (key, value);
  }

  @ExceptionHandler (ClientConfigurationKeyNotFoundException.class)
  @ResponseStatus (NOT_FOUND)
  @ResponseBody
  public String handleNotFound (ClientConfigurationKeyNotFoundException e) {
    return e.getLocalizedMessage ();
  }
}
