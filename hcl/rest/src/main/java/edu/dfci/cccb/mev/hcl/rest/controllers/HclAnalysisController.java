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

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.hcl.domain.contract.HclBuilder;
import edu.dfci.cccb.mev.hcl.domain.contract.InvalidAlgorithmException;
import edu.dfci.cccb.mev.hcl.domain.contract.InvalidMetricException;

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

  private @Getter @Setter (onMethod = @_ (@Inject)) Dataset dataset;
  private @Getter @Setter @Inject Provider<HclBuilder> build;

  // @RequestMapping (value = "/analyze/hcl/" + ANALYSIS_URL_ELEMENT + "(" +
  // DIMENSION_URL_ELEMENT + ","
  // + METRIC_URL_ELEMENT + "," + LINKAGE_URL_ELEMENT + ")",
  // method = POST)
  // @ResponseStatus (OK)
  // public void start (final @PathVariable (ANALYSIS_MAPPING_NAME) String name,
  // final @PathVariable (DIMENSION_MAPPING_NAME) Dimension dimension,
  // final @PathVariable (METRIC_MAPPING_NAME) Metric metric,
  // final @PathVariable (LINKAGE_MAPPING_NAME) Linkage linkage) throws
  // DatasetNotFoundException,
  // InvalidDimensionTypeException {
  // // TODO: inject a factory instead of manual injection
  // final HclBuilder builder = new SimpleTwoDimensionalHclBuilder
  // ().nodeBuilder (nodeBuilder)
  // .dataset (dataset)
  // .dimension (dimension)
  // .linkage (linkage)
  // .metric (metric);
  //
  // log.debug ("Running HCL on " + dataset);
  //
  // new Thread () {
  // /* (non-Javadoc)
  // * @see java.lang.Thread#run() */
  // @Override
  // public void run () {
  // try {
  // dataset.analyses ().put (builder.name (name).build ());
  // } catch (DatasetException e) {
  // log.warn ("Could not cluster hierarchically", e);
  // }
  // }
  // }.run (); // .start (); TODO: async analysis
  // }

  @NoArgsConstructor
  @AllArgsConstructor
  @Accessors (fluent = true)
  public static class HclDto {
    @JsonProperty @Getter private String name;
    @JsonProperty(required=false) @Getter private String dimension;
    @JsonProperty @Getter private String metric;
    @JsonProperty @Getter private String linkage;
    @JsonProperty(required=false) @Getter private List<String> columns;
    @JsonProperty(required=false) @Getter private List<String> rows;
  }

  @RequestMapping (value = "/analyze/hcl", method = POST)
  @ResponseStatus (OK)
  public Analysis startJson (@RequestBody final HclDto dto) throws DatasetNotFoundException,
                                                           InvalidDimensionTypeException,
                                                           InvalidAlgorithmException,
                                                           InvalidMetricException {

	
    // TODO: inject a factory instead of manual injection
    final HclBuilder builder = build.get ().dataset (dataset)                                    
                                    .linkage (dto.linkage ())
                                    .metric (dto.metric ())
                                    .columns(dto.columns())
                                    .rows(dto.rows())
                                    .name (dto.name ());
    if(dto.dimension()!=null && 
    		(dto.dimension().equalsIgnoreCase(Dimension.Type.ROW.toString()) || 
    		dto.dimension().equalsIgnoreCase(Dimension.Type.COLUMN.toString()))){    	
    	builder.dimension (dataset.dimension (Type.from (dto.dimension ())));    	
    }else{
    	builder.dimension(dataset.dimension(Dimension.Type.ROW));
    	builder.dimension(dataset.dimension(Dimension.Type.COLUMN));
    }
    log.debug ("Running HCL on " + dataset);
    return builder.buildAsync ();

    // new Thread () {
    // /* (non-Javadoc)
    // * @see java.lang.Thread#run() */
    // @Override
    // public void run () {
    // try {
    // dataset.analyses ().put (builder.name (dto.name()).build ());
    // } catch (DatasetException e) {
    // log.warn ("Could not cluster hierarchically", e);
    // }
    // }
    // }.run (); // .start (); TODO: async analysis

  }
  
}
