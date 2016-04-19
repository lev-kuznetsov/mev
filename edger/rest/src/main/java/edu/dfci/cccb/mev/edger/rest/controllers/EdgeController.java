package edu.dfci.cccb.mev.edger.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.Set;

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
import edu.dfci.cccb.mev.edger.domain.EdgeBuilder;

@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class EdgeController {

  private @Inject Dataset dataset;
  private @Inject Provider<EdgeBuilder> edge;

  private static class EdgeParams {
    @JsonProperty String name;
    @JsonProperty Set<String> experiment;
    @JsonProperty Set<String> control;
    @JsonProperty String method; // one of: "fdr", "holm", "hochberg", "BH",
                                 // "BY", "bonferroni", "none"
  }

  @RequestMapping (value = "/analyze/voom", method = POST)
  @ResponseStatus (OK)
  public Analysis start (@RequestBody EdgeParams p) throws DatasetException {
    return edge.get ()
               .experiment (p.experiment)
               .control (p.control)
               .dataset (dataset)
               .method (p.method)
               .name (p.name)
               .buildAsync ();
  }
}
