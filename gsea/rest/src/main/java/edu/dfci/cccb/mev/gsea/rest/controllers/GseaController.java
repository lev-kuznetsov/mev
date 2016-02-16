package edu.dfci.cccb.mev.gsea.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.gsea.domain.GseaBuilder;
import edu.dfci.cccb.mev.gsea.domain.LimmaEntry;

@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class GseaController {

  public static class GseaParameters {
    private @JsonProperty String name;
    private @JsonProperty List<LimmaEntry> limma;
    private @JsonProperty String organism;
    private @JsonProperty String pAdjustMethod;
    private @JsonProperty int minGSSize;
    private @JsonProperty double adjValueCutoff;
  }

  private @Inject Provider<GseaBuilder> builder;

  @RequestMapping (value = "/analyze/gsea", method = POST)
  @ResponseStatus (OK)
  public Analysis start (@RequestBody GseaParameters params) throws DatasetException {
    return builder.get ()
                  .limma (params.limma)
                  .adjValueCutoff (params.adjValueCutoff)
                  .name (params.name)
                  .minGSSize (params.minGSSize)
                  .organism (params.organism)
                  .pAdjustMethod (params.pAdjustMethod)
                  .buildAsync ();
  }
}
