package edu.dfci.cccb.mev.annotation.elasticsearch.perf;

import javax.inject.Inject;

import org.databene.contiperf.junit.ContiPerfRule;
import org.elasticsearch.client.Client;
import org.junit.Rule;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;

public class AbstractElasticsearchPerfTest {

  @Rule public ContiPerfRule i = new ContiPerfRule ();
  
  @Inject protected IndexAdminHelper adminHelper;
  @Inject protected BulkProcessorFactory bulkProcessorFactory;
  @Inject protected Client client;
  protected int invocationsCount = 0;

  public AbstractElasticsearchPerfTest () {
    ElasticSearchConfiguration config = new ElasticSearchConfiguration ();
    client = config.getTransportClient ();
    adminHelper = config.getIndexAdminHelper (client);
    bulkProcessorFactory = config.getBulkProcessorFactory (client);
  }

}