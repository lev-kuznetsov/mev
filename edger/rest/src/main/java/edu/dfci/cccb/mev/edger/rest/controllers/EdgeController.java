package edu.dfci.cccb.mev.edger.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;
import javax.inject.Provider;

import edu.dfci.cccb.mev.edger.domain.Edge;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

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

  @RequestMapping (value = "/analyze/edger/{name}", method = RequestMethod.PUT)
  @ResponseStatus (OK)
  public Analysis start (@RequestBody Edge.EdgeParams p) throws DatasetException {
    return edge.get ()
               .params(p)
               .buildAsync ();
  }
}
