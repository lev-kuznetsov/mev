package edu.dfci.cccb.mev.dataset.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.DATASET;
import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.DATASET_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.DIMENSION;
import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.DIMENSION_URL_ELEMENT;

import java.util.Collection;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;

@Log4j
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT + "/" + DIMENSION_URL_ELEMENT )
public class SelectionsController {
    
  @RequestMapping(value="/selection", method=RequestMethod.GET)
  public Collection<String> getSelectionNames(@PathVariable(DATASET) Dataset dataset,
                                              @PathVariable(DIMENSION) Dimension dimension) throws InvalidDimensionTypeException{
//    if(log.isDebugEnabled ())
//      log.debug ("Returning selection names for {} dimension {} ", dataset.name (), dimension.type ());
    return dimension.selections ().list ();
  }  
  
  @RequestMapping(value="/selections", method=RequestMethod.GET)
  public Collection<Selection> getSelections(@PathVariable(DATASET) Dataset dataset,
                                             @PathVariable(DIMENSION) Dimension dimension) throws InvalidDimensionTypeException{
//    if(log.isDebugEnabled ())
//      log.debug ("Returning selections for {} dimension {} ", dataset.name (), dimension.type ());
//    
    return dimension.selections ().getAll ();    
  }
  
  @RequestMapping(value="/selection/{name}", method=RequestMethod.GET)
  public Selection getSelection(@PathVariable("name") String name, 
                                @PathVariable(DATASET) Dataset dataset,
                                @PathVariable(DIMENSION) Dimension dimension) throws InvalidDimensionTypeException, SelectionNotFoundException{
//    if(log.isDebugEnabled ())
//      log.debug ("Returning selections for {} dimension {} ", dataset.name (), dimension.type ());
    
    return dimension.selections ().get(name);    
  }
}
