package edu.dfci.cccb.mev.normalization.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;
import javax.inject.Provider;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.dfci.cccb.mev.normalization.domain.Normalization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.normalization.domain.NormalizationBuilder;

import java.util.Arrays;

@Log4j
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class NormalizationController {

  private @Inject Provider<NormalizationBuilder> builder;
  private @Inject Dataset dataset;
  private @Inject Workspace workspace;

  @RequestMapping (value = "/analyze/normalization/{name}", method = PUT)
  @ResponseStatus (OK)
  public Analysis start (@RequestBody Normalization.NormalizationParameters p) throws DatasetException {
    return builder.get ().dataset (dataset).params (p).buildAsync ();
  }

  @Accessors(fluent=true)
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NormalizationExportParams{
    private @Getter @JsonProperty String name;
  }

  @RequestMapping(value="/analyze/normalization/{name}/export", method = PUT)
  @ResponseStatus(OK)
  public void exportNormalized(@RequestBody NormalizationExportParams dto) throws Exception{
    Normalization analysis = (Normalization) dataset.analyses().get(dto.name());
    Dataset normalized = analysis.result();
    normalized.rename(analysis.params().exportName());
    log.info ("Returning NORMALIZED dataset "
              + normalized.name() + " of type " + normalized.getClass () + " implementing "
              + Arrays.asList (normalized.getClass ().getInterfaces ()));
    workspace.put(normalized);
//    return normalized;
  }

}
