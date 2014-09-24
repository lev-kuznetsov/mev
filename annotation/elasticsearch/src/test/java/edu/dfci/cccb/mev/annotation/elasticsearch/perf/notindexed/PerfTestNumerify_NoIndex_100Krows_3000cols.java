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

public class PerfTestNumerify_NoIndex_100Krows_3000cols extends PerfTestNumerify_NoIndex_Base {

// run 1
//  samples: 1
//  max:     534,785 
//  average: 534785.0 ~9 min
//  median:  534785

  
  @Override
  protected long getNUMCOLS () {
    return 3000;
  }

  @Override
  protected int getNUMROWS () {
    return 100*1000;
  }

  @Override  
  protected int getPAGE_SIZE () {   
//    return 1000;
    return 100;
  }
  
  @Override @Test  
  @PerfTest(invocations=1, threads=1, timer=ConstantTimer.class, timerParams={1000})
  public void testNumerify () throws IndexAdminException, IOException, IndexLoaderException, InterruptedException {
     super.testNumerify ();
  }
}
