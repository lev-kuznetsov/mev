package edu.dfci.cccb.mev.annotation.elasticsearch.perf.rows6K;

import java.io.IOException;

import lombok.extern.log4j.Log4j;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.timer.ConstantTimer;
import org.junit.Test;
import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

@Log4j
public class PerfTestNumerify_06Krows_500cols extends AbstractPerfTestNumerify {

// run 1
//  max:     28885
//  average: 16956.666666666668
//  median:  9810
  
// run 2 
//  max:     11982
//  average: 11358.666666666666
//  median:  10807
  
// run 3
//  max:     73412
//  average: 37163.333333333336
//  median:  18029
  
// ** after fix
// run 4
//  samples: 3
//  max:     9000
//  average: 8609.666666666666
//  median:  8167
// run 5
//  samples: 3
//  max:     9554
//  average: 8716.0
//  median:  8137
// run 6
//  samples: 3
//  max:     10937
//  average: 9654.666666666666
//  median:  8724
// run 7
//  samples: 3
//  max:     9017
//  average: 8287.0
//  median:  7706
  
  @Override
  protected long getNUMCOLS () {
    return 500;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

  @Test 
  @PerfTest(invocations=3, threads=1, timer=ConstantTimer.class, timerParams={1000})
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
