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
import static edu.dfci.cccb.mev.dataset.rest.resolvers.DimensionPathVariableMethodArgumentResolver.DIMENSION_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.SelectionPathVariableMethodArgumentResolver.SELECTION_MAPPING_NAME;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.SelectionPathVariableMethodArgumentResolver.SELECTION_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selections;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT + "/" + DIMENSION_URL_ELEMENT)
@Log4j
@Scope (SCOPE_REQUEST)
public class SelectionController {

  private @Getter @Setter @Inject Dimension dimension;
  private @Getter @Setter @Inject SelectionBuilder builder;

  private @Inject Workspace workspace;
  private @Inject Dataset dataset;
  private @Inject DatasetBuilder datasetBuilder;

  @RequestMapping (value = "/selections", method = GET)
  public Selections all () {
    return dimension.selections ();
  }

  @RequestMapping (value = "/selection", method = GET)
  public Collection<String> list () {
    return dimension.selections ().list ();
  }

  @RequestMapping (value = "/selection/" + SELECTION_URL_ELEMENT, method = GET)
  public Selection get (@PathVariable (SELECTION_MAPPING_NAME) Selection selection) {
    return selection;
  }

  @RequestMapping (value = "/selection/" + SELECTION_URL_ELEMENT, method = PUT)
  @ResponseStatus (OK)
  public void select (@PathVariable (SELECTION_MAPPING_NAME) String name,
                      @RequestParam (value = "selectionDescription", required = false) String description,
                      @RequestParam (value = "selectionColor", required = false) String color,
                      @RequestParam ("keys") List<String> keys) {
    Selection selection = builder.name (name)
                                 .property ("selectionDescription", description)
                                 .property ("selectionColor", color)
                                 .keys (keys)
                                 .build ();
    if (log.isDebugEnabled ())
      log.debug ("Adding selection " + selection);
    dimension.selections ().put (selection);
  }

  @RequestMapping (value = "/selection", method = POST)
  @ResponseStatus (OK)
  public void addSelection (@RequestBody Selection selection) {
    if (log.isDebugEnabled ())
      log.debug ("Adding selection " + selection);
    dimension.selections ().put (selection);
  }

  @RequestMapping (value = "/selection", method = PUT)
  @ResponseStatus (OK)
  public void updateSelection (@RequestBody Selection selection) {
    if (log.isDebugEnabled ())
      log.debug ("Updating selection " + selection);

    try {
      dimension.selections().remove(selection.name());
    } catch (SelectionNotFoundException e) {
      if (log.isDebugEnabled ())
        log.debug (String.format("Selection %s not found, will add new", selection));
    }
    dimension.selections ().put (selection);
  }
  
  @RequestMapping (value = "/selection/{name}", method = DELETE)
  @ResponseStatus (OK)
  public void deleteByName (@PathVariable("name") String name) throws SelectionNotFoundException {
    if (log.isDebugEnabled ())
      log.debug ("Deleting selection " + name);
    dimension.selections ().remove(name);
  }
  
  @RequestMapping (value = "/selection/export", method = POST)
  @ResponseStatus (OK)
  public void exportSelection (@RequestBody Selection selection) throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException {
    if (log.isDebugEnabled ())
      log.debug ("============>Export new dataset" + selection);
//    workspace.put (datasetBuilder.build (new DatasetExportRawInput (selection.name(), dataset, selection, dimension.type ())));
    List<String> colums = dimension.type() == Type.COLUMN ? selection.keys() : dataset.dimension(Type.COLUMN).keys();
    List<String> rows = dimension.type() == Type.ROW ? selection.keys() : dataset.dimension(Type.ROW).keys();    
    workspace.put (dataset.subset(selection.name(), colums, rows));    
  }

  @RequestMapping (value = "/selection/" + SELECTION_URL_ELEMENT, method = POST)
  @ResponseStatus (OK)
  public void export (@PathVariable (SELECTION_MAPPING_NAME) Selection selection,
                      @RequestParam ("name") String name) throws InvalidDimensionTypeException,
                                                         SelectionNotFoundException,
                                                         InvalidCoordinateException,
                                                         DatasetBuilderException,
                                                         InvalidDatasetNameException,
                                                         IOException {
    dataset.exportSelection (name, dimension.type (), selection.name (), workspace, datasetBuilder);
  }
}
