package edu.dfci.cccb.mev.annotation.elasticsearch.perf.notindexed;

public class PerfTestNumerify_NoIndex_100Krows_10cols extends PerfTestNumerify_NoIndex_Base {
  
// run 1
//  samples: 3
//  max:     4813
//  average: 4112.333333333333
//  median:  3590
  
  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 100*1000;
  }

}
