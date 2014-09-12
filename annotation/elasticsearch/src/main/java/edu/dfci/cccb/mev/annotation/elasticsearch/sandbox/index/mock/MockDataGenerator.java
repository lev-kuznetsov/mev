package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.mock;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import scala.collection.concurrent.Debug;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminHelperImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.SimpleBulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;

@Log4j
public class MockDataGenerator {
  
  private final Client client;
  private final IndexAdminHelper adminHelper;  
    
  public MockDataGenerator(Client client){
    this.client=client;
    this.adminHelper=new IndexAdminHelperImpl (client);
  }
  
  
  public Map<String, Object> generateRandomDoc(long numOfFields){
    Map<String, Object> doc = new LinkedHashMap<String, Object> ();
    Random rand = new Random (); 
    for(Integer i=0;i<numOfFields;i++){
      doc.put (String.format("field%s", i), Integer.toString (rand.nextInt (1000)));
    } 
    return doc;
  }
  
  public void writeDataset(String indexName, String documentType, long numOfDocs, long numOfFields, BulkProcessor bulkProcessor){
    log.debug (String.format("*** Writing data to index %s", indexName));
    adminHelper.createIndex (indexName);
    for(Integer i=0;i<numOfDocs;i++){
      IndexRequestBuilder indexRequestBuilder = client.prepareIndex (indexName, documentType, i.toString ())
              .setSource (generateRandomDoc (numOfFields));
      bulkProcessor.add (indexRequestBuilder.request ());
    }      
  }
  
  @SneakyThrows()
  public void  overwriteDataset (String indexName, String documentType, long numOfDocs, long numOfFields, BulkProcessor bulkProcessor){    
    if(adminHelper.exists (indexName)){
      log.debug (String.format("*** Index %s already exists. ", indexName));
      adminHelper.deleteIndex (indexName);
      log.debug (String.format("*** Index %s deleted. ", indexName));
    }
    writeDataset (indexName, documentType, numOfDocs, numOfFields, bulkProcessor);      
  }
  
  public void  overwriteDataset (long numOfDocs, long numOfFields, BulkProcessor bulkProcessor){    
    String indexName = adminHelper.dummyName(numOfDocs, numOfFields);    
    writeDataset (indexName, "dummy_type", numOfDocs, numOfFields, bulkProcessor);      
  }
}
