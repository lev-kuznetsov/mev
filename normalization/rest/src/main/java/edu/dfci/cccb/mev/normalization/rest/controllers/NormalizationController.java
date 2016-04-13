package edu.dfci.cccb.mev.normalization.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.normalization.domain.NormalizationBuilder;

@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class NormalizationController {

  private static class NormalizationParameters {
    private @JsonProperty String method;
    private @JsonProperty String name;
  }

  private @Inject Provider<NormalizationBuilder> builder;
  private @Inject Dataset dataset;
  private @Inject Workspace workspace;

  @RequestMapping (value = "/analyze/normalization", method = POST)
  @ResponseStatus (OK)
  public Analysis start (@RequestBody NormalizationParameters p) throws DatasetException {
    return builder.get ().dataset (dataset).method (p.method).workspace (workspace).exportName (p.name).buildAsync ();
  }
}
