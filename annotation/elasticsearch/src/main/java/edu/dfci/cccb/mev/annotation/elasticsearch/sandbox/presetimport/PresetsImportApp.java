package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.presetimport;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.contract.CsvRiverAnnotationPutRequest;
import edu.dfci.cccb.mev.annotation.elasticsearch.csvriver.request.CsvRiverPutRequestTcgaPreset;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsFilesConfiguration;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.TcgaPreset;
import edu.dfci.cccb.mev.presets.tools.loader.PresetsLoaderConfiguration;



@Log4j
@Component
public class PresetsImportApp {

  @Inject Presets presets;
  @Inject Client client;
  
  private String responseToString(IndexResponse response){
      return String.format ("RESPONSE: _index: %s, _type: %s, _id: %s, _version",
                            response.getIndex (), response.getType (), response.getId (), response.getVersion ());
  }
  
  
  public static void main (String[] args) throws InterruptedException {
    try(final AnnotationConfigApplicationContext springContext = new AnnotationConfigApplicationContext ()){
      springContext.register (ProbeAnnotationsFilesConfiguration.class);
      springContext.register (PresetsLoaderConfiguration.class);      
      springContext.register (ElasticSearchConfiguration.class);
      log.debug("***** PresetsImportApp REFRESH ****");
      springContext.refresh ();
      
      final PresetsImportApp app = springContext.getBean (PresetsImportApp.class);
      log.debug("***** PresetsImportApp RUNING ****");
      app.run(args);
    }
    
  }
  
  public void run(String[] args){
    log.debug("***** PresetsImportApp RUN ****");
    for(Preset preset : presets.getAll ()){
      importPreset(preset);
    }
  }

  public void importPreset(Preset preset){
    log.debug("Importing preset:"+preset.name ());
    
    CsvRiverAnnotationPutRequest putRequest = new CsvRiverPutRequestTcgaPreset((TcgaPreset)preset);
    log.debug("JSON: "+ putRequest.toString ());
    IndexResponse response = client.prepareIndex ("_river", 
                                                  putRequest.getRiverName (),
                                                  "_meta")
            .setSource (putRequest.toString ())
            .execute ()
            .actionGet ();
    responseToString(response);
  }
  
  
 
  @SuppressWarnings ("unused")
  private void testGet(){
    //  SearchResponse response = client.prepareSearch().execute().actionGet();
    GetResponse response = client.prepareGet("tcga_clinical", "acc", "7f5ff6b6-a509-4d17-ba1d-f4852fe8aee4_miR_gene_expression")
            .execute()
            .actionGet();
    log.debug("Get Response:"+response.getSourceAsString ());
   
  }
  
  @PreDestroy
  public void cleanup(){
    if(log.isDebugEnabled ())
      log.debug("Closing client");
    client.close ();  
  }
}
