package edu.dfci.cccb.mev.voom.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Provider;

import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.voom.domain.VoomBuilder;

@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class VoomController {

  private @Inject Dataset dataset;
  private @Inject Provider<VoomBuilder> voom;

  private static class VoomParams {
    @JsonProperty String name;
    @JsonProperty Collection<String> experiment;
    @JsonProperty Collection<String> control;
  }

  @RequestMapping (value = "/analyze/voom", method = POST)
  @ResponseStatus (OK)
  public Analysis start (@RequestBody VoomParams params) throws DatasetException {
    return voom.get ()
               .experiment (params.experiment)
               .control (params.control)
               .dataset (dataset)
               .name (params.name)
               .buildAsync ();
  }

  private static class VoomParamsPUT {
    @JsonProperty String name;
    @JsonProperty Selection experiment;
    @JsonProperty Selection control;
  }
  
  @RequestMapping (value = "/analyze/voom/{name}", method = PUT)
  @ResponseStatus (OK)
  public Analysis put (@RequestBody VoomParamsPUT params) throws DatasetException {
    return voom.get ()
            .experiment (params.experiment.keys())
            .control (params.control.keys())
            .dataset (dataset)
            .name (params.name)
            .buildAsync ();
  }
}
