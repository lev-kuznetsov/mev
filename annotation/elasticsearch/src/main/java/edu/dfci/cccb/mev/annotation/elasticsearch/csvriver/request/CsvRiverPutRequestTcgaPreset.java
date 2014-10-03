package edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.request;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Delegate;
import lombok.SneakyThrows;
import edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.contract.CsvRiverAnnotationPutRequest;
import edu.dfci.cccb.mev.presets.contract.TcgaPreset;

public class CsvRiverPutRequestTcgaPreset implements CsvRiverAnnotationPutRequest {
  
  @Delegate CsvRiverPutRequest request;
  
  @SneakyThrows({URISyntaxException.class})
  public CsvRiverPutRequestTcgaPreset(TcgaPreset preset){
    
    Path csvColumnFile = Paths.get(preset.descriptor ().columnUrl ().toURI ());
    csvColumnFile.getParent ();    
    this.request = new CsvRiverPutRequest ("tcga_clinical", preset.disease (), 
                                           "tcga_clinical_river_"+preset.disease (), csvColumnFile.getParent ().toString (), 
                                           csvColumnFile.getFileName ().toString ());    
  }
  
  @Override
  public String toString () {
    return request.toString ();
  }
}
