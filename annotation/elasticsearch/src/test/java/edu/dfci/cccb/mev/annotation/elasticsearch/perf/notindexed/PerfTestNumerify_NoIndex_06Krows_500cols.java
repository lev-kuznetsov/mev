package edu.dfci.cccb.mev.annotation.elasticsearch.perf.notindexed;

public class PerfTestNumerify_NoIndex_06Krows_500cols extends PerfTestNumerify_NoIndex_Base {

// run 1
//  samples: 3
//  max:     4413
//  average: 3457.3333333333335
//  median:  2611
  
  
  @Override
  protected long getNUMCOLS () {
    return 500;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

}
