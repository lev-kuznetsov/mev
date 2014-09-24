package edu.dfci.cccb.mev.annotation.elasticsearch.perf.notindexed;

import static org.junit.Assert.*;

import java.io.IOException;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.timer.ConstantTimer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

public class PerfTestNumerify_NoIndex_06Krows_1000cols extends PerfTestNumerify_NoIndex_Base {

// run 1
//  samples: 3
//  max:     5257
//  average: 5049.0
//  median:  4701
// run 2
//  samples: 3
//  max:     6646
//  average: 5488.333333333333
//  median:  4531
// run 3 
//  samples: 10
//  max:     8957
//  average: 5171.5
//  median:  4423
  
  @Override
  protected long getNUMCOLS () {
    return 1000;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

  @Override @Test  
  @PerfTest(invocations=10, threads=1, timer=ConstantTimer.class, timerParams={1000})
  public void testNumerify () throws IndexAdminException, IOException, IndexLoaderException, InterruptedException {
    super.testNumerify ();
  }
  
}
