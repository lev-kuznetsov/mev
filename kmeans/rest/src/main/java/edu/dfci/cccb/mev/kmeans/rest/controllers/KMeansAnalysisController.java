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
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.dfci.cccb.mev.dataset.domain.contract.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

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

  @Accessors(fluent = true)
  public static class KmeansParamsDTO{
    @JsonProperty @Getter private String name;
    @JsonProperty @Getter private String dimension;
    @JsonProperty @Getter private Integer k;
    @JsonProperty @Getter private String metric;
    @JsonProperty @Getter private Integer iterations;
  }

  @RequestMapping(value="/analyze/kmeans/{name}", method = PUT)
  public Analysis put(final @RequestBody KmeansParamsDTO params) throws InvalidDimensionTypeException{
    Dimension.Type type = Dimension.Type.from(params.dimension());
    Dimension dimension = dataset.dimension(type);
    return kmeans.name (params.name()).k (params.k())
            .dimension (dimension)
            .metric (params.metric())
            .dataset (dataset)
            .buildAsync (); 
  }

  @RequestMapping (value = "/analysis/" + ANALYSIS_URL_ELEMENT + "/kmeans",
                   method = POST)
  public Dimension apply (@PathVariable (ANALYSIS_MAPPING_NAME) KMeans analysis) throws DatasetException {
    return analysis.apply ();
  }
}
