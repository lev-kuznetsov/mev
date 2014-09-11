package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.TypeScroll;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.CsvIndexLoaderConfig;

@Log4j
@RequiredArgsConstructor
public class TypeCopierImpl implements TypeCopier {
  private final Client client;
  private final BulkProcessor bulkProcessor;
  IndexAdminHelper indexAdminHelper;
  
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.TypeCopier#process(java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public void process(String sourceIndexName, String sourceDocumentType, String targetIndexName){
    TypeScroll scroll = new TypeScrollImpl (client, sourceIndexName, sourceDocumentType);
    for(SearchHit hit : scroll){
      IndexRequestBuilder requestBuilder = client.prepareIndex (targetIndexName, sourceDocumentType, hit.getId ());    
      requestBuilder.setSource (hit.source ());
      bulkProcessor.add (requestBuilder.request ());
    }
    bulkProcessor.flush ();
  }
}
