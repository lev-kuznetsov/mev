package edu.dfci.cccb.mev.annotation.elasticsearch.perf.touch;

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

public class PerfTestNumerify_Touch_100Krows_10cols extends PerfTestNumerify_Touch_Base {

// run 1
//  samples: 3
//  max:     22629
//  average: 9868.333333333334
//  median:  2560
// run 2, 3 - crashed with heap maxed out
// OK - the "touch" approach does not work.
//  samples: 2
//  max:     7495
//  average: 4966.0
//  median:  2437

  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 100*1000;
  }

  @Override
  @Test 
  @PerfTest(invocations=10, threads=1, timer=ConstantTimer.class, timerParams={1000})
  public void testNumerify () throws IndexAdminException, IOException, IndexLoaderException, InterruptedException {    
    super.testNumerify ();
  }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    