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
package edu.dfci.cccb.mev.deseq.rest.controllers;

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.deseq.domain.contract.DESeqBuilder;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class DESeqAnalysisController {

  private @Getter @Setter @Inject Dataset dataset;
  private @Getter @Setter @Inject DESeqBuilder deseq;

  @RequestMapping (value = "/analyze/deseq/{name}",
                   method = POST)
  @ResponseStatus (OK)
  public Analysis start (final @PathVariable ("name") String name,
                     final @RequestParam ("experiment") String experiment,
                     final @RequestParam ("control") String control) throws DatasetException {
    return deseq.name (name)
        .dataset (dataset)
        .experiment (dataset.dimension (COLUMN).selections ().get (experiment))
        .control (dataset.dimension (COLUMN).selections ().get (control))
        .buildAsync ();
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Accessors(fluent = true)
  public static class DeseqDTO{
    @JsonProperty @Getter private String name;
    @JsonProperty @Getter private Selection control;
    @JsonProperty @Getter private Selection experiment;
  }

  @RequestMapping (value="/analyze/deseq/{name}",
  method = PUT)
  @ResponseStatus (OK)
  public Analysis put (final @RequestBody DeseqDTO params){
    return deseq.name (params.name())
            .dataset (dataset)
            .experiment (params.experiment())
            .control (params.control())
            .buildAsync ();
  }
}
