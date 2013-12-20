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
package edu.dfci.cccb.mev.hcl.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver.ANALYSIS_MAPPING_NAME;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver.ANALYSIS_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.DimensionPathVariableMethodArgumentResolver.DIMENSION_MAPPING_NAME;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.DimensionPathVariableMethodArgumentResolver.DIMENSION_URL_ELEMENT;
import static edu.dfci.cccb.mev.hcl.rest.resolvers.LinkagePathVariableMethodArgumentResolver.LINKAGE_MAPPING_NAME;
import static edu.dfci.cccb.mev.hcl.rest.resolvers.LinkagePathVariableMethodArgumentResolver.LINKAGE_URL_ELEMENT;
import static edu.dfci.cccb.mev.hcl.rest.resolvers.MetricPathVariableMethodArgumentResolver.METRIC_MAPPING_NAME;
import static edu.dfci.cccb.mev.hcl.rest.resolvers.MetricPathVariableMethodArgumentResolver.METRIC_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.hcl.domain.contract.Hcl;
import edu.dfci.cccb.mev.hcl.domain.contract.HclBuilder;
import edu.dfci.cccb.mev.hcl.domain.contract.Linkage;
import edu.dfci.cccb.mev.hcl.domain.contract.Metric;
import edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder;
import edu.dfci.cccb.mev.hcl.domain.simple.SimpleTwoDimensionalHclBuilder;

/**
 * @author levk
 * 
 */
@RestController
@ToString
@Log4j
@RequestMapping (value = "/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class HclAnalysisController {

  private @Getter @Setter (onMethod = @_ (@Inject)) NodeBuilder nodeBuilder;
  private @Getter @Setter (onMethod = @_ (@Inject)) Dataset dataset;

  @RequestMapping (value = "/analyze/hcl/" + ANALYSIS_URL_ELEMENT + "(" + DIMENSION_URL_ELEMENT + ","
                           + METRIC_URL_ELEMENT + "," + LINKAGE_URL_ELEMENT + ")",
                   method = POST)
  @ResponseStatus (OK)
  public void start (final @PathVariable (ANALYSIS_MAPPING_NAME) String name,
                     final @PathVariable (DIMENSION_MAPPING_NAME) Dimension dimension,
                     final @PathVariable (METRIC_MAPPING_NAME) Metric metric,
                     final @PathVariable (LINKAGE_MAPPING_NAME) Linkage linkage) throws DatasetNotFoundException,
                                                                                InvalidDimensionTypeException {
    // TODO: inject a factory instead of manual injection
    final HclBuilder builder = new SimpleTwoDimensionalHclBuilder ().nodeBuilder (nodeBuilder)
                                                                    .dataset (dataset)
                                                                    .dimension (dimension)
                                                                    .linkage (linkage)
                                                                    .metric (metric);

    log.debug ("Running HCL on " + dataset);

    new Thread () {
      /* (non-Javadoc)
       * @see java.lang.Thread#run() */
      @Override
      public void run () {
        try {
          dataset.analyses ().put (builder.name (name).build ());
        } catch (DatasetException e) {
          log.warn ("Could not cluster hierarchically", e);
        }
      }
    }.run (); // .start (); TODO: async analysis
  }

  @RequestMapping (value = "/analysis/" + ANALYSIS_URL_ELEMENT,
                   method = POST)
  public Dimension apply (@PathVariable (ANALYSIS_MAPPING_NAME) Hcl analysis) throws DatasetException {
    return analysis.apply ();
  }
}
