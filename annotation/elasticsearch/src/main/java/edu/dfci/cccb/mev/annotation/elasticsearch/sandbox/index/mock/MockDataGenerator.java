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
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminHelperImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.SimpleBulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;

@Log4j
public class MockDataGenerator {
  
  private final Client client;
  private final IndexAdminHelper adminHelper;  
  private final DynamicTemplateBuilder templateBuilder;
  
  public MockDataGenerator(Client client, DynamicTemplateBuilder templateBuilder){
    this.client=client;
    this.adminHelper=new IndexAdminHelperImpl (client);
    this.templateBuilder=templateBuilder;
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
    //TODO: create index with default schema
    if(this.templateBuilder!=null){
      Map<String, Object> mapping = new LinkedHashMap<String, Object> ();
      mapping.put("dynamic_templates", this.templateBuilder.build ());
      adminHelper.createIndex (indexName, documentType, mapping);
    }else{
      adminHelper.createIndex (indexName);
    }
    
    for(Integer i=0;i<numOfDocs;i++){
      IndexRequestBuilder indexRequestBuilder = client.prepareIndex (indexName, documentType, i.toString ())
              .setSource (generateRandomDoc (numOfFields));
      bulkProcessor.add (indexRequestBuilder.request ());
    } 
    bulkProcessor.flush ();    
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
