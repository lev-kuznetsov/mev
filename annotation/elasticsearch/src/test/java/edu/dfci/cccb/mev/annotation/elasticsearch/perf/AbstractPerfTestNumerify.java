package edu.dfci.cccb.mev.annotation.elasticsearch.perf;

import java.io.IOException;

import javax.inject.Inject;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.timer.ConstantTimer;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.mock.MockDataGenerator;

@Log4j
public abstract class AbstractPerfTestNumerify extends AbstractElasticsearchPerfTest {

  protected String getDocumentType (){
    return "dummy_type";
  };
  protected int getCONCURRENT_THREADS () {
    return 8;
  }
  protected int getPAGE_SIZE () {
    return 100;
  }

  protected DynamicTemplateBuilder getTemplateBuilder(){
    return null;
  }

  
  protected abstract long getNUMCOLS ();
  protected abstract int getNUMROWS ();

  @Inject protected DynamicTemplateBuilder templateBuilder;
  @Before
  @SuppressWarnings ("unused")
  public void setup() throws InterruptedException{
    
    if(true || !adminHelper.exists (adminHelper.dummyName (getNUMROWS(), getNUMCOLS()))){
      log.debug("POPULATED DUMMY DATA");
      adminHelper.deleteIndex (adminHelper.dummyName (getNUMROWS(), getNUMCOLS()));
      getTemplateBuilder();
      MockDataGenerator mockgen = new MockDataGenerator (client, getTemplateBuilder());
      mockgen.overwriteDataset (getNUMROWS(), getNUMCOLS(),
                                  bulkProcessorFactory.create (
                                  bulkProcessorFactory.calculateBulkRows (getNUMCOLS()), getCONCURRENT_THREADS()));
    }
    waitForUpdateCompletion ();
  }
  
  @Test 
  @PerfTest(invocations=3, threads=1, timer=ConstantTimer.class, timerParams={1000})
  public void testNumerify () throws IndexAdminException, IOException, IndexLoaderException, InterruptedException {    
    adminHelper.numerifyField (adminHelper.dummyName (getNUMROWS(), getNUMCOLS()), getDocumentType (), "field"+invocationsCount, 
                               bulkProcessorFactory.create (
                                 bulkProcessorFactory.calculateBulkRows (getNUMCOLS()), getCONCURRENT_THREADS()),
                                 getPAGE_SIZE()
                                 );
    log.debug ("EXECUTED "+invocationsCount++);
    waitForUpdateCompletion ();
  }

  @SneakyThrows({InterruptedException.class})
  protected void waitForUpdateCompletion() {
    while(true){
      CountRequestBuilder countRequestBuilder = client.prepareCount (adminHelper.dummyName (getNUMROWS(), getNUMCOLS())).setTypes (getDocumentType ());
      CountResponse countResponse = countRequestBuilder.execute ().actionGet ();
      if(countResponse.getCount ()==getNUMROWS ())
        break;
      Thread.sleep (500);
    }
  }
  
  @After
  public void afterLastInvocation() throws InterruptedException{
    waitForUpdateCompletion();
  }


}