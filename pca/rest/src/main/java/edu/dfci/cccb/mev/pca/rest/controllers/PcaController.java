package edu.dfci.cccb.mev.pca.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.pca.domain.PcaBuilder;

@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class PcaController {

  private @Inject Dataset data;
  private @Inject Provider<PcaBuilder> pca;

  @RequestMapping (value = "/analyze/pca", method = POST)
  @ResponseStatus (OK)
  public void start (@RequestBody Map<String, String> params) throws DatasetException {
    data.analyses ().put (pca.get ().dataset (data).name (params.get ("name")).build ());
  }
}
