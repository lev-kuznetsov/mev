package edu.dfci.cccb.mev.annotation.elasticsearch.perf.alltypes;

public class PerfTestNumerify_AllTypes_06Krows_3000cols extends PerfTestNumerify_AllTypes_Base {

// run 1 - timed out after first run, 1 index 1022M
//  samples: 1
//  max:     345073
//  average: 345073.0
//  median:  345073
  
  @Override
  protected long getNUMCOLS () {
    return 3000;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

}
