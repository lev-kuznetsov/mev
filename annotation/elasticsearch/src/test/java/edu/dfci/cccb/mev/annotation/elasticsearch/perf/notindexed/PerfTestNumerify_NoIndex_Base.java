package edu.dfci.cccb.mev.annotation.elasticsearch.perf.notindexed;

import java.io.IOException;

import lombok.extern.log4j.Log4j;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.timer.ConstantTimer;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder_NoIndex;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

@Log4j
public abstract class PerfTestNumerify_NoIndex_Base extends AbstractPerfTestNumerify {

  public PerfTestNumerify_NoIndex_Base () {
    super ();
  }

  @Override
  protected DynamicTemplateBuilder getTemplateBuilder () {
    return new DynamicTemplateBuilder_NoIndex ();
  }
  
  @Override @Test  
  @PerfTest(invocations=10, threads=1, timer=ConstantTimer.class, timerParams={1000})
  public void testNumerify () throws IndexAdminException, IOException, IndexLoaderException, InterruptedException {    
    adminHelper.numerifyField (adminHelper.dummyName (getNUMROWS(), getNUMCOLS()), getDocumentType (), "field"+invocationsCount, 
                               bulkProcessorFactory.create (
                                 bulkProcessorFactory.calculateBulkRows (getNUMCOLS())
                                 , getCONCURRENT_THREADS()),
                                 getPAGE_SIZE()
                                 );
    log.debug ("EXECUTED "+invocationsCount++);
    waitForUpdateCompletion ();
  }
}