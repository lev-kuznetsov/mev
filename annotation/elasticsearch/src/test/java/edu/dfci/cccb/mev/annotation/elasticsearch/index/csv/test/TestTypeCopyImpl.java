package edu.dfci.cccb.mev.annotation.elasticsearch.index.csv.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.log4j.Log4j;

import org.elasticsearch.action.admin.indices.refresh.RefreshRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.TypeCopier;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.TypeCopierImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.TypeScrollImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.SimpleBulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.TypeScroll;

@Log4j
public class TestTypeCopyImpl extends AbstractTestWithElasticSearch {

  private final String index_name="test_index";
  private final String type_name="test_type";
  private final String index_name_copy=index_name+"_copy";
  
  
  @Test
  public void testCopy () throws InterruptedException {
    final int numOfDocs = 11;
  
    Map<String, Integer> validIds = new HashMap <String, Integer> (numOfDocs); 
    for(Integer i=0;i<numOfDocs;i++){
      IndexResponse indexResponse = client.prepareIndex (index_name, type_name, i.toString ())
              .setSource (String.format ("{id:%s, data:\"abc\"}", i))
      .execute ().actionGet ();
    
      assertTrue(indexResponse.isCreated ());
      validIds.put (i.toString (), i);
    } 
    
    RefreshRequestBuilder refreshRequestBuilder = new RefreshRequestBuilder (client.admin ().indices ());
    refreshRequestBuilder.setIndices (index_name).execute ().actionGet ();
    Thread.sleep (1*1000);
    
    final SimpleBulkProcessorFactory bulkFactory=new SimpleBulkProcessorFactory (client);
    TypeCopier copier = new TypeCopierImpl (client, bulkFactory.create (2, 1));
    copier.process (index_name, type_name, index_name_copy);
    
    refreshRequestBuilder = new RefreshRequestBuilder (client.admin ().indices ());
    refreshRequestBuilder.setIndices (index_name_copy).execute ().actionGet ();
    Thread.sleep (1*1000);
    
    TypeScroll scanner = new TypeScrollImpl (client, index_name_copy, type_name, 2);
    int totalCount=0;
    for(SearchHit hit : scanner){
      log.debug("**** HIT " + hit.getSourceAsString ());
      log.debug("**** REMOVING " + hit.getId ());
      validIds.remove (hit.getId ().toString ());      
      totalCount++;
    }
    assertEquals (numOfDocs, totalCount);   
    assertTrue(validIds.isEmpty ());
  }

}
