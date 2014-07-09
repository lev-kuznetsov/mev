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
package edu.dfci.cccb.mev.stats.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.stats.domain.contract.Hypothesis;
import edu.dfci.cccb.mev.stats.domain.contract.WilcoxonBuilder;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class WilcoxonController {

  private @Getter @Setter @Inject Dataset dataset;
  private @Getter @Setter @Inject WilcoxonBuilder wilcoxon;

  @RequestMapping (value = "/analyze/wilcoxon/{name}",
                   method = GET)
  @ResponseStatus (OK)
  public void start (@PathVariable ("name") String name,
                     @RequestParam (value = "confidentInterval", defaultValue = "false") boolean confidentInterval,
                     @RequestParam ("first") String first,
                     @RequestParam (value = "second", required = false) String second,
                     @RequestParam (value = "pair", defaultValue = "false") boolean pair,
                     @RequestParam ("hypothesis") String hypothesis) throws DatasetException {
    dataset.analyses ().put (wilcoxon.name (name)
                                     .confidentInterval (confidentInterval)
                                     .first (first)
                                     .second (second)
                                     .hypothesis (Hypothesis.from (hypothesis))
                                     .build ());
  }
}
