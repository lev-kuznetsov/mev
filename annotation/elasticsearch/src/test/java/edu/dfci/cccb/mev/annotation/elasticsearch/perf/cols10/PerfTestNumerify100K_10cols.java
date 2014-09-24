package edu.dfci.cccb.mev.annotation.elasticsearch.perf.cols10;

import static org.junit.Assert.*;

import java.io.IOException;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.timer.ConstantTimer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

public class PerfTestNumerify100K_10cols extends AbstractPerfTestNumerify {

  @Override
  protected long getNUMCOLS () {
    return 10;
  }
// run 1
//  max:     10403
//  average: 9246.666666666666
//  median:  8436
// run 2 heapsize: 2g
//  samples: 10
//  max:     5844
//  average: 4892.3
//  median:  4716

  
  @Override
  protected int getNUMROWS () {
    return 100*1000;
  }
  
  @Test 
  @PerfTest(invocations=10, threads=1, timer=ConstantTimer.class, timerParams={1000})
  public void testNumerify () throws IndexAdminException, IOException, IndexLoaderException, InterruptedException {    
    super.testNumerify ();
  }


  
}
