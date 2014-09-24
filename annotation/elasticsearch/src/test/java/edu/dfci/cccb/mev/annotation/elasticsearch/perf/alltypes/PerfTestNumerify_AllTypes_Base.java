package edu.dfci.cccb.mev.annotation.elasticsearch.perf.alltypes;

import java.io.IOException;

import javax.inject.Inject;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.databene.contiperf.timer.ConstantTimer;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractElasticsearchPerfTest;
import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder_AllTypes;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder_NoIndex;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminHelperImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.SimpleBulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.mock.MockDataGenerator;

@Log4j
public abstract class PerfTestNumerify_AllTypes_Base extends AbstractElasticsearchPerfTest  {

  protected DynamicTemplateBuilder templateBuilder;
  
  public PerfTestNumerify_AllTypes_Base(){
    super();
    setupTemplateBuilder();
  }  
  protected void setupTemplateBuilder () {
    templateBuilder = new DynamicTemplateBuilder_AllTypes ();
  }
  protected int getCONCURRENT_THREADS () {
    return 8;
  }
  protected int getPAGE_SIZE () {
    return 100;
  }

  protected long getNUMCOLS () {
    return 10;
  }

  protected int getNUMROWS () {
    return 10;
  }
  protected String getDocumentType (){
    return "dummy_type";
  };

  @SneakyThrows({InterruptedException.class})
  protected void waitForUpdateCompletion(int inv) {
    while(true){
      CountRequestBuilder countRequestBuilder = client.prepareCount (dummyIndexName (getNUMROWS(), getNUMCOLS(), inv)).setTypes (getDocumentType ());
      CountResponse countResponse = countRequestBuilder.execute ().actionGet ();
      if(countResponse.getCount ()==getNUMROWS ())
        break;
      Thread.sleep (500);
    }
  }
  
  private String dummyIndexName(int rows, long cols, int invocationCount){
    return String.format("dummy_r%sc%si%s", rows, cols, invocationCount);
  }
  
  @Test  
  @PerfTest(invocations=3, threads=1, timer=ConstantTimer.class, timerParams={1000})
  public void testLoad () throws IndexAdminException, IOException, IndexLoaderException, InterruptedException {    
    MockDataGenerator mockgen = new MockDataGenerator (client, this.templateBuilder);
    mockgen.writeDataset (dummyIndexName (getNUMROWS(), getNUMCOLS (), invocationsCount), getDocumentType ()
                          , getNUMROWS (), getNUMCOLS ()
                          , bulkProcessorFactory.create (
                            bulkProcessorFactory.calculateBulkRows (getNUMCOLS()), getCONCURRENT_THREADS()));
    waitForUpdateCompletion(invocationsCount);
    invocationsCount++;
  }
  
  @After
  public void afterLastInvocation() throws InterruptedException{
    waitForUpdateCompletion(invocationsCount-1);
  }
  
}