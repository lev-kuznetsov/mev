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
import edu.dfci.cccb.mev.stats.domain.contract.FisherBuilder;
import edu.dfci.cccb.mev.stats.domain.contract.Hypothesis;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class FisherAnalysisController {

  private @Getter @Setter @Inject Dataset dataset;
  private @Getter @Setter @Inject FisherBuilder fisher;

  @RequestMapping (value = "/analyze/fisher/{name}",
                   method = GET)
  @ResponseStatus (OK)
  public void start (@PathVariable ("name") String name,
                     @RequestParam ("m") int m,
                     @RequestParam ("n") int n,
                     @RequestParam ("s") int s,
                     @RequestParam ("t") int t,
                     @RequestParam ("hypothesis") String hypothesis,
                     @RequestParam (value = "simulate", defaultValue = "false") boolean simulate) throws DatasetException {
    dataset.analyses ().put (fisher.name (name)
                                   .n (n)
                                   .m (m)
                                   .s (s)
                                   .t (t)
                                   .simulate (simulate)
                                   .hypothesis (Hypothesis.from (hypothesis))
                                   .build ());
  }
}
