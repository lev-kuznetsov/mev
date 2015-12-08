package edu.dfci.cccb.mev.genemad.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.genemad.domain.impl.RserveGeneMADAnalysisBuilder;

@Log4j
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class GeneMADAnalysisController {
  private @Getter @Setter @Inject Dataset dataset;
  private @Getter @Setter @Inject Provider<RserveGeneMADAnalysisBuilder> builderProvider;

  @RequestMapping (value = "/analyze/genemad/{name}", method = PUT, consumes = MediaType.APPLICATION_JSON_VALUE)  
  public Analysis startSurvivalAnalysis (final @PathVariable ("name") String name) throws DatasetException {
    log.debug ("##### Gene MAD" + name);
    RserveGeneMADAnalysisBuilder builder = builderProvider.get ();
    return builder.name (name).buildAsync ();
  }
}
