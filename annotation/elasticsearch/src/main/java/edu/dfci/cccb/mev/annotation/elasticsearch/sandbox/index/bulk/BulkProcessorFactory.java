package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk;

import org.elasticsearch.action.bulk.BulkProcessor;

public interface BulkProcessorFactory {

  public BulkProcessor create (int bulkSize, int concurrentRequests);

}