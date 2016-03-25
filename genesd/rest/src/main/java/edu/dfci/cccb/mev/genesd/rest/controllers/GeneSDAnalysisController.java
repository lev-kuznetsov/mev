package edu.dfci.cccb.mev.genesd.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
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
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.genesd.domain.impl.RserveGeneSDAnalysisBuilder;

@Log4j
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class GeneSDAnalysisController {
  private @Getter @Setter @Inject Dataset dataset;
  private @Getter @Setter @Inject Provider<RserveGeneSDAnalysisBuilder> builderProvider;

  @RequestMapping (value = "/analyze/genesd/{name}", method = PUT, consumes = MediaType.APPLICATION_JSON_VALUE)  
  public Analysis startSurvivalAnalysis (final @PathVariable ("name") String name) throws DatasetException {
    log.debug ("##### Gene SD" + name);
    RserveGeneSDAnalysisBuilder builder = builderProvider.get ();
    return builder.name (name).buildAsync ();
  }
}
