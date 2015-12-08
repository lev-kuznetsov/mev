package edu.dfci.cccb.mev.topgo.rest.controllers;

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
import edu.dfci.cccb.mev.topgo.domain.TopGoBuilder;

@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
@Log4j
public class TopGoController {

  private @Inject Dataset dataset;
  private @Inject Provider<TopGoBuilder> topGo;

  @ToString
  @JsonIgnoreProperties (ignoreUnknown = true)
  private static class TopGoParameters {
    private @JsonProperty String name;
    private @JsonProperty String[] genelist;
    private @JsonProperty String species;
    private @JsonProperty String goType;
    private @JsonProperty String testType;
    private @JsonProperty String pAdjust;
    private @JsonProperty int nodeSize;
  }

  @RequestMapping (value = "/analyze/topgo", method = POST)
  @ResponseStatus (OK)
  public Analysis start (@RequestBody TopGoParameters parameters) throws DatasetException {
    log.debug ("Requested topGO analysis with parameters " + parameters);
    return topGo.get ()
                .species (parameters.species)
                .goType (parameters.goType)
                .testType (parameters.testType)
                .pAdjust (parameters.pAdjust)
                .nodeSize (parameters.nodeSize)
                .genelist (Arrays.asList (parameters.genelist))
                .dataset (dataset)
                .name (parameters.name).buildAsync ();
  }
}
