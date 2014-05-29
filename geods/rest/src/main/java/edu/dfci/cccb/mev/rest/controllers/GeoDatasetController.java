package edu.dfci.cccb.mev.rest.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.gzip.GzipTsvInput;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.dataset.rest.assembly.tsv.MultipartTsvInput;

@Log4j
@RestController
@RequestMapping(value="/geods")
public class GeoDatasetController {

  private @Getter @Setter @Inject Workspace workspace;
  private @Getter @Setter @Inject DatasetBuilder builder;
  
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Accessors(fluent=true)  
  public static class GeoImportDto{
    @JsonProperty private String gds;
    @JsonProperty private String gpl;
//    @JsonProperty private List<String> samples;
    @JsonProperty private Map<String, String> samples;
    @JsonProperty private String datasetUrl;
  }
  
  @RequestMapping(value="/import/{name:"+Dataset.VALID_DATASET_NAME_REGEX+"}", method=RequestMethod.PUT)
  public void importGeoDataset(@PathVariable("name") String name, @RequestBody GeoImportDto dto ) throws MalformedURLException, DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException{
    log.debug ("GeoImportDto:"+dto);
    GZIPInputStream zip;
    RawInput gdsInput = new GzipTsvInput(new URL(dto.datasetUrl+"/soft/GDS"+dto.gds+".soft.gz"));
    
    Dataset dataset = builder.build (gdsInput);
    if (log.isDebugEnabled ())
      log.debug ("Uploaded " + dataset);
    workspace.put (dataset);
  }
}
