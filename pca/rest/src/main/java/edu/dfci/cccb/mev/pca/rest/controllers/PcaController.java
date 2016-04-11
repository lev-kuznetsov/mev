package edu.dfci.cccb.mev.pca.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.pca.domain.PcaBuilder;

@Log4j
@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class PcaController {

  private @Inject Dataset data;
  private @Inject Provider<PcaBuilder> pca;

  @RequestMapping (value = "/analyze/pca", method = POST)
  @ResponseStatus (OK)
  public Analysis start (@RequestBody Map<String, String> params) throws DatasetException {
    return pca.get ().dataset (data).name (params.get ("name")).buildAsync ();
  }
  
  @Accessors(fluent=true)
  @ToString
  private static class PcaDto{
	  @JsonProperty @Getter private String name;
	  @JsonProperty(required=false) @Getter private List<String> sampleList;
	  @JsonProperty(required=false) @Getter private List<String> geneList;
  }
  
  @RequestMapping (value = "/analyze/pca/{name}", method = PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus (OK)
  public Analysis put (final @PathVariable ("name") String name, @RequestBody PcaDto dto) throws DatasetException {
    return pca.get ().dataset (data).name (name).sampleList(dto.sampleList()).geneList(dto.geneList()).buildAsync ();
  }
}
