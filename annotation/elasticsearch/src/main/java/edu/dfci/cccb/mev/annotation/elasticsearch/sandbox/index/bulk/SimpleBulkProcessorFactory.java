package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

@Log4j
@RequiredArgsConstructor
public class SimpleBulkProcessorFactory implements BulkProcessorFactory {

  private final Client client;  
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory#create(int, int)
   */
  @Override
  public BulkProcessor create(int bulkSize, int concurrentRequests){
    return BulkProcessor.builder(client, new BulkProcessor.Listener() {
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
  }).setBulkActions(bulkSize).setConcurrentRequests(concurrentRequests).build();
  }
  
  @Override
  public int calculateBulkRows(long numOfCols){
    double colSizeBytes = numOfCols*Integer.SIZE/8;
    double minBulkSizeBytes=5e6;
    int bulkSizeRows = (int) (minBulkSizeBytes/colSizeBytes);
    return bulkSizeRows;
  }

  @Override
  public BulkProcessor create (long numOfCols, int concurrentRequests) {
    int bulkSizeRows = calculateBulkRows (numOfCols);    
    return create(bulkSizeRows, concurrentRequests);
  }

}
