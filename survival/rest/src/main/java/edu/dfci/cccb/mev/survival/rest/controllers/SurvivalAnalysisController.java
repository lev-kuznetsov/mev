package edu.dfci.cccb.mev.survival.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalAnalysisBuilder;
import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalParams;

@Log4j
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class SurvivalAnalysisController {
  private @Getter @Setter @Inject Dataset dataset;
  private @Getter @Setter @Inject Provider<SimpleSurvivalAnalysisBuilder> builderProvider;
  
  @RequestMapping (value = "/analyze/survival", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  public Analysis startSurvivalAnalysis (@RequestBody SimpleSurvivalParams dto) throws DatasetException {
    log.debug("##### Survival" + dto);
//    SimpleSurvivalAnalysisBuilder builder = new SimpleSurvivalAnalysisBuilder (); 
    SimpleSurvivalAnalysisBuilder builder = builderProvider.get ();
    return builder.params (dto).buildAsync ();
  }
}
