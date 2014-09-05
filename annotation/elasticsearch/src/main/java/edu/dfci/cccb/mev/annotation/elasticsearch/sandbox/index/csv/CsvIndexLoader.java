package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import au.com.bytecode.opencsv.CSVReader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminHelperImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParser;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderConfig;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

@Log4j
public class CsvIndexLoader implements IndexLoader {
  
  Client client;
  CsvIndexLoaderConfig config;
  BulkProcessor bulkProcessor;
  IndexAdminHelper indexAdminHelper;
  
  public CsvIndexLoader(CsvIndexLoaderConfig config, Client client){
    //new TransportClient ().addTransportAddress (new InetSocketTransportAddress ("anton-masha.dfci.harvard.edu", 9300));
     this.client = client;
     this.config = config;
     this.indexAdminHelper = new IndexAdminHelperImpl (client);
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
    IndexDocumentParser parser = new CsvIndexDocumentParser (entry, this.config);
    XContentBuilder mappingBuilder = parser.getMapping ();
    indexAdminHelper.createIndex (config.indexName (), config.typeName (), mappingBuilder);    
    for(XContentBuilder jsonBuilder : parser ){
      processDataLine(jsonBuilder);
    }
  }
    
  private void processDataLine(XContentBuilder jsonBuilder) throws IOException{    
    IndexRequestBuilder requestBuilder = client.prepareIndex (config.indexName (), config.typeName ());    
    requestBuilder.setSource (jsonBuilder);
    bulkProcessor.add (requestBuilder.request ());    
  }
  
  String getId(String[] line) {
    int index = config.csvFields().indexOf(config.idField());
    return line[index];
  }
}
