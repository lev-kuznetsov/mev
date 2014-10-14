package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.TypeScroll;

@RequiredArgsConstructor
public class TypeUpdateTrigger {
  private final Client client;
  private final BulkProcessor bulkProcessor;
  private final int pageSize;
  
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.TypeCopier#process(java.lang.String, java.lang.String, java.lang.String)
   */
    public void process(String sourceIndexName, String sourceDocumentType){    
    TypeScroll scroll = new TypeScrollImpl (client, sourceIndexName, sourceDocumentType, pageSize);
    for(SearchHit hit : scroll){
      UpdateRequestBuilder requestBuilder = client.prepareUpdate (sourceIndexName, sourceDocumentType, hit.getId ());
      requestBuilder.setDoc ("{}");      
      bulkProcessor.add (requestBuilder.request ());
    }
    bulkProcessor.flush ();    
  }
}
