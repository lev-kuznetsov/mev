package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.presetimport;

import java.net.URL;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.MacsImportConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.CsvIndexLoader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavIndexDocumentParserFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavLoaderConfig;

@Log4j
@Component
public class MacsImportApp {

  @Inject Client client;
  @Inject IndexAdminHelper adminHelper;
  @Inject EavLoaderConfig config;
  
  
  public static void main(String [] args) throws IndexLoaderException{
    
    try(AnnotationConfigApplicationContext springContext = new AnnotationConfigApplicationContext ()){
      springContext.register (ElasticSearchConfiguration.class);
      springContext.register (MacsImportConfiguration.class);      
      springContext.refresh ();
      
      MacsImportApp app = springContext.getBean (MacsImportApp.class);
      app.run(args);
    }
  }

  public void run(String[] args) throws IndexLoaderException{
    if(!adminHelper.exists (config.indexName ()) || true){
      log.debug("*** Deleting " + config.indexName ());
      adminHelper.deleteIndex (config.indexName());
      
      log.debug(String.format("*** Loading %s from folder %s file %s", config.indexName()
                              , config.folderPath (), config.fileFilter ()));
      CsvIndexLoader loader = new CsvIndexLoader (config, client, new EavIndexDocumentParserFactory ());
      loader.process ();
    }
  }
}
