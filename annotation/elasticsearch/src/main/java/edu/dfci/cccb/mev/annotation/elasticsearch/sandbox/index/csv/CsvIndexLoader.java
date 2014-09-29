package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminHelperImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParser;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParserFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParserIterator;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

@Log4j
public class CsvIndexLoader implements IndexLoader {
  
  Client client;
  CsvIndexLoaderConfig config;
  BulkProcessor bulkProcessor;
  IndexAdminHelper indexAdminHelper;
  IndexDocumentParserFactory parserFactory;
  Map<String, String> existingIds;
  public CsvIndexLoader(CsvIndexLoaderConfig config, Client client, IndexDocumentParserFactory parserFactory){
     this.client = client;
     this.config = config;
     this.parserFactory=parserFactory;
     this.indexAdminHelper = new IndexAdminHelperImpl (client);
     this.existingIds=new HashMap<String, String>();
     this.bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
       @Override
       public void beforeBulk(long executionId, BulkRequest request) {
           log.info(String.format("Going to execute new bulk composed of %d actions", request.numberOfActions()));
       }

       @Override
       public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
         log.warn("Error executing bulk", failure);
       }

       @Override
       public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
         log.info(String.format("Executed bulk composed of %d actions", request.numberOfActions()));
       }
   }).setBulkActions(config.bulkSize()).setConcurrentRequests(config.concurrentRequests()).build();
  }
  
  @Override
  @SneakyThrows({IOException.class})
  public void process () throws IndexLoaderException {
    try(DirectoryStream<Path> stream = config.getDirectoryStream ()){
      for(Path entry : stream){        
        processFile(entry);
      }
    }
  }
  
  @Override
  @SneakyThrows({UnsupportedEncodingException.class})
  public void processFile(Path entry) throws IOException{
    log.info (String.format("Processing file %s", entry));
    IndexDocumentParser parser = parserFactory.create (entry, this.config);
    XContentBuilder mappingBuilder = parser.getMapping ();
    indexAdminHelper.createIndex (config.indexName (), config.typeName (), mappingBuilder);
    IndexDocumentParserIterator it = parser.iterator ();
    while(it.hasNext ()){
      XContentBuilder jsonBuilder = it.next ();
      processDataLine(jsonBuilder, it.getId ());
    }    
    bulkProcessor.flush ();
  }
    
  private void processDataLine(XContentBuilder jsonBuilder, String id) throws IOException{    
    
    if(existingIds.containsKey (id)){
      UpdateRequestBuilder requestBuilder = client.prepareUpdate (config.indexName (), config.typeName (), id);    
      requestBuilder.setDoc (jsonBuilder);
      bulkProcessor.add (requestBuilder.request ());
    }else{
      IndexRequestBuilder requestBuilder = client.prepareIndex (config.indexName (), config.typeName (), id);    
      requestBuilder.setSource (jsonBuilder);
      bulkProcessor.add (requestBuilder.request ());
    }
    
  }
  
  String getId(String[] line) {
    int index = config.csvFields().indexOf(config.entityIdFields ());
    return line[index];
  }
}
