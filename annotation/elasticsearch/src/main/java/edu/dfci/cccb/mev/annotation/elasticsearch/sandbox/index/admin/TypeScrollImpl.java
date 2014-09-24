package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import java.util.Iterator;

import lombok.extern.log4j.Log4j;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.TypeScroll;

@Log4j
public class TypeScrollImpl implements Iterable<SearchHit>, TypeScroll {

  private final Client client;
  private final String indexName;
  private final String documentType;
  private final int pageSize;  
  private final int scrollWaitTimeMilli=60*1000;
  
  public TypeScrollImpl (Client client, String indexName, String documentType) {
    this(client, indexName, documentType, 1000);
  }

  public TypeScrollImpl (Client client, String indexName, String documentType, int pageSize) {
    this.client=client;
    this.indexName=indexName;
    this.documentType=documentType;
    this.pageSize=pageSize;
  }

  
  @Override
  public Iterator<SearchHit> iterator () {
    SearchRequestBuilder scrollSearchBuilder = client.prepareSearch (indexName)            
            .setTypes (documentType)
            .setSearchType (SearchType.SCAN)
            .setScroll (new TimeValue (scrollWaitTimeMilli))
            .setSize (pageSize);    
    SearchResponse initScrollResponse = scrollSearchBuilder.execute ().actionGet ();    
    log.debug("*** TOTAL_HITS: "+initScrollResponse.getHits ().getTotalHits ());
    return new ScrollIterator (initScrollResponse);
  }

  
  private class ScrollIterator implements Iterator<SearchHit>{

    private SearchResponse searchResponse;
    private Iterator<SearchHit> pageIterator;    
    private boolean endOfScroll=false;
    
    ScrollIterator(SearchResponse initScrollResponse){
      //fetch the inital page
      this.searchResponse = initScrollResponse;
      fetchPage ();
    }
    
    private void fetchPage(){
      //scrollId will be null if the initial serch result is empty
      log.debug ("**** Fetching page for SCROLL_ID:"+searchResponse.getScrollId ());
      if(searchResponse.getScrollId ()==null){        
        endOfScroll=true;
        return;
      }
      searchResponse=client.prepareSearchScroll (searchResponse.getScrollId ())
              .setScroll(new TimeValue(scrollWaitTimeMilli))
              .execute ().actionGet ();      
      pageIterator=searchResponse.getHits ().iterator ();
      //if the page returned empty, nothing left to scroll
      endOfScroll=!pageIterator.hasNext ();        
    }
    
    @Override
    public boolean hasNext () {
      if(endOfScroll) return false;
      if(pageIterator.hasNext ()==false){
        //end of page, get the next one
        fetchPage ();
      }        
      return pageIterator.hasNext ();
    }

    @Override
    public SearchHit next () {
        if(hasNext())
          return pageIterator.next ();
        else
          return null;
    }

    @Override
    public void remove () {
      throw new UnsupportedOperationException ();      
    }
    
  }

}
