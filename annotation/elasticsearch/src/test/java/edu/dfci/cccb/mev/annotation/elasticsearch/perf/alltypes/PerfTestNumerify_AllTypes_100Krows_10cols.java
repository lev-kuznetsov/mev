package edu.dfci.cccb.mev.annotation.elasticsearch.perf.alltypes;

public class PerfTestNumerify_AllTypes_100Krows_10cols extends PerfTestNumerify_AllTypes_Base {

// run 1 each index about 45M
//  samples: 3
//  max:     17080
//  average: 14201.333333333334
//  median:  10171
  
  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 100*1000;
  }

}
