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
package edu.dfci.cccb.mev.dataset.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.DATASET_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.DIMENSION_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.SELECTION;
import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.SELECTION_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionBuilder;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT + "/" + DIMENSION_URL_ELEMENT + "/selection")
@Log4j
public class SelectionController {

  private @Getter @Setter (onMethod = @_ (@Inject)) Dimension dimension;
  private @Getter @Setter (onMethod = @_ (@Inject)) SelectionBuilder builder;

  @RequestMapping (method = GET)
  public Collection<String> list () {
    return dimension.selections ().list ();
  }

  @RequestMapping (value = "/" + SELECTION_URL_ELEMENT, method = GET)
  public Selection get (@PathVariable (SELECTION) Selection selection) {
    return selection;
  }

  @RequestMapping (value = "/" + SELECTION_URL_ELEMENT, method = PUT)
  @ResponseStatus (OK)
  public void select (@PathVariable (SELECTION) String name,
                      @RequestParam (value = "properties", required = false) Properties properties,
                      @RequestParam ("keys") List<String> keys) {
    Selection selection = builder.name (name)
                                 .properties (properties == null ? new Properties () : properties)
                                 .keys (keys)
                                 .build ();
    if (log.isDebugEnabled ())
      log.debug ("Adding selection " + selection);
    dimension.selections ().put (selection);
  }
}
