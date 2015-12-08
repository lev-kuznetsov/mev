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
package edu.dfci.cccb.mev.kmeans.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver.ANALYSIS_MAPPING_NAME;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver.ANALYSIS_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.DimensionPathVariableMethodArgumentResolver.DIMENSION_MAPPING_NAME;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.DimensionPathVariableMethodArgumentResolver.DIMENSION_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeans;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeansBuilder;

/**
 * @author levk
 * 
 */
@RestController
@ToString (exclude = { "dataset", "kmeans" })
@RequestMapping (value = "/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class KMeansAnalysisController {

  private @Getter @Setter @Inject Dataset dataset;
  private @Inject KMeansBuilder kmeans;

  @RequestMapping (value = "/analyze/kmeans/" + ANALYSIS_URL_ELEMENT + "("
                           + "dimension=" + DIMENSION_URL_ELEMENT + ","
                           + "k={k},"
                           + "metric={metric},"
                           + "iterations={iterations},"
                           + "convergence={convergence})",
                   method = POST)
  @ResponseStatus (OK)
  public Analysis start (final @PathVariable (ANALYSIS_MAPPING_NAME) String name,
                     final @PathVariable (DIMENSION_MAPPING_NAME) Dimension dimension,
                     final @PathVariable ("k") int k,
                     final @PathVariable ("metric") String metric,
                     final @PathVariable ("iterations") int iterations,
                     final @PathVariable ("convergence") double convergenceDelta) throws DatasetException {
    return kmeans.name (name).k (k)
                     .dimension (dimension)
                     .metric (metric)
                     .dataset (dataset)
                     .buildAsync ();
  }

  @RequestMapping (value = "/analysis/" + ANALYSIS_URL_ELEMENT + "/kmeans",
                   method = POST)
  public Dimension apply (@PathVariable (ANALYSIS_MAPPING_NAME) KMeans analysis) throws DatasetException {
    return analysis.apply ();
  }
}
