package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.TypeCopier;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.TypeScroll;

@RequiredArgsConstructor
public class TypeCopierImpl implements TypeCopier {
  private final Client client;
  private final BulkProcessor bulkProcessor;
  private final int pageSize;
  
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.TypeCopier#process(java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public void process(String sourceIndexName, String sourceDocumentType, String targetIndexName){
    TypeScroll scroll = new TypeScrollImpl (client, sourceIndexName, sourceDocumentType, pageSize);
    for(SearchHit hit : scroll){
      IndexRequestBuilder requestBuilder = client.prepareIndex (targetIndexName, sourceDocumentType, hit.getId ());    
      requestBuilder.setSource (hit.source ());
      bulkProcessor.add (requestBuilder.request ());
    }
    bulkProcessor.flush ();    
  }
}
