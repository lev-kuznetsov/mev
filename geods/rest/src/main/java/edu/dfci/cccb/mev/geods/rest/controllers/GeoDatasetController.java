package edu.dfci.cccb.mev.geods.rest.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

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
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.gzip.GzipTsvInput;
import edu.dfci.cccb.mev.goeds.domain.contract.GeoDatasetBuilder;
import edu.dfci.cccb.mev.goeds.domain.contract.GeoSource;
import edu.dfci.cccb.mev.goeds.domain.dataset.ftp.GeoSourceFTP;

@Log4j
@RestController
@RequestMapping(value="/geods")
public class GeoDatasetController {

  private @Inject @Getter @Setter Workspace workspace;
  private @Inject @Getter @Setter @Named ("GeoDatasetBuilder") GeoDatasetBuilder builder;
  private @Inject @Getter @Setter @Named("GeoFtpRootUrl") URL geoFtpRoot;
  
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
    
    GeoSource geoSource = new GeoSourceFTP (dto.gds, dto.gpl, dto.samples, geoFtpRoot);    
    RawInput gdsInput = new GzipTsvInput(geoSource.getDatasetUrl ());    
    Dataset dataset = builder.setGeoSource (geoSource).build (gdsInput);
    if (log.isDebugEnabled ())
      log.debug ("Uploaded " + dataset);
    workspace.put (dataset);
  }
}
