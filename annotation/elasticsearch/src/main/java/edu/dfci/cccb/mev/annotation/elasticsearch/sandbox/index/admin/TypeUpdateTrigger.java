package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.TypeCopier;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.TypeScroll;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.CsvIndexLoaderConfig;

@Log4j
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
