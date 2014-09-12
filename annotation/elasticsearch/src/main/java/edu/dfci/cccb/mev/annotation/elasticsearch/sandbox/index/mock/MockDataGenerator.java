package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.mock;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminHelperImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.SimpleBulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;

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
      doc.put (String.format("field%s", i), Integer.toString (rand.nextInt ()));
    } 
    return doc;
  }
  
  public void writeDataset(String indexName, String documentType, long numOfDocs, long numOfFields, BulkProcessor bulkProcessor){    
    for(Integer i=0;i<numOfDocs;i++){
      IndexRequestBuilder indexRequestBuilder = client.prepareIndex (indexName, documentType, i.toString ())
              .setSource (generateRandomDoc (numOfFields));
      bulkProcessor.add (indexRequestBuilder.request ());
    }      
  }
  
  public void  overwriteDataset (String indexName, String documentType, long numOfDocs, long numOfFields, BulkProcessor bulkProcessor){
    if(adminHelper.exists (indexName)){
      adminHelper.deleteIndex (indexName);
    }    
    writeDataset (indexName, documentType, numOfDocs, numOfFields, bulkProcessor);      
  }
}
