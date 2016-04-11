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

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static java.util.Arrays.asList;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.List;

import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT + "/data")
@ToString
@Log4j
@Scope (SCOPE_REQUEST)
public class DataController {

  private @Inject Dataset dataset;
  private @Inject Workspace workspace;
  
  @RequestMapping (method = GET)
  public Dataset dataset () {
    if (log.isDebugEnabled ())
      log.debug ("Returning data "
                 + dataset + " of type " + dataset.getClass () + " implementing "
                 + asList (dataset.getClass ().getInterfaces ()));
    return dataset;
  }
  
  @RequestMapping(method=GET, value="/values", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public Values values() throws Exception{
    if (log.isDebugEnabled ())
      log.debug ("Returning VALUES "
                 + dataset + " of type " + dataset.getClass () + " implementing "
                 + asList (dataset.getClass ().getInterfaces ()));
    
    return dataset.values();       
  }
  
  @RequestMapping(method=GET, value="/values")
  public Values valuesJson() throws Exception{
    if (log.isDebugEnabled ())
      log.debug ("Returning VALUES as JSON"
                 + dataset + " of type " + dataset.getClass () + " implementing "
                 + asList (dataset.getClass ().getInterfaces ()));
    
    return dataset.values();       
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Accessors(fluent=true)
  public static class SubsetRequest{
    private @Getter @JsonProperty  String name;;
    private @Getter @JsonProperty(required=false)  List<String> columns;
    private @Getter @JsonProperty(required=false)  List<String> rows;
  }
  
  @RequestMapping(method=POST, value="/subset")
  public Dataset subset(@RequestBody SubsetRequest dto) throws Exception{
    Dataset subset = dataset.subset (dto.name(), dto.columns (), dto.rows ());
    if (log.isDebugEnabled ())
      log.debug ("Returning SUBSET "
                 + dataset + " of type " + subset.getClass () + " implementing "
                 + asList (subset.getClass ().getInterfaces ()));
    
    return subset; 
  }
  
  @RequestMapping(method=POST, value="/subset/export")
  public void exportSubset(@RequestBody SubsetRequest dto) throws Exception{
    Dataset subset = dataset.subset (dto.name(), dto.columns (), dto.rows ());
    if (log.isDebugEnabled ())
      log.debug ("Returning SUBSET "
                 + dataset + " of type " + subset.getClass () + " implementing "
                 + asList (subset.getClass ().getInterfaces ()));    
    workspace.put(subset); 
  }
  
}
