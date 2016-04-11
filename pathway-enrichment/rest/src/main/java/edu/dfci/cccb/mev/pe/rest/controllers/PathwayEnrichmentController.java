package edu.dfci.cccb.mev.pe.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.pe.domain.PathwayEnrichment.PathwayEnrichmentParameters;
import edu.dfci.cccb.mev.pe.domain.PathwayEnrichmentBuilder;

@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
@Log4j
public class PathwayEnrichmentController {

  private @Inject Dataset dataset;
  private @Inject Provider<PathwayEnrichmentBuilder> pe;

  

  @RequestMapping (value = "/analyze/pe", method = POST)
  @ResponseStatus (OK)
  public Analysis start (@RequestBody PathwayEnrichmentParameters parameters) throws DatasetException {
    log.debug ("Requested topGO analysis with parameters " + parameters);
    return pe.get ()
             .organism (parameters.organism())
             .genelist (Arrays.asList (parameters.genelist()))
             .pAdjustMethod (parameters.pAdjustMethod())
             .minGSSize (parameters.minGSSize())
             .pvalueCutoff (parameters.pvalueCutoff())             
             .name (parameters.name())
             .params (parameters).buildAsync ();
  }
}
