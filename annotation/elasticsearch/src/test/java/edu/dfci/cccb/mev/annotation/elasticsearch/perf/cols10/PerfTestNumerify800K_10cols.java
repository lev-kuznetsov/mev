package edu.dfci.cccb.mev.annotation.elasticsearch.perf.cols10;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;

public class PerfTestNumerify800K_10cols extends AbstractPerfTestNumerify {

//run 1
//  samples: 3
//  max:     14411
//  average: 13796.0
//  median:  13391
  
  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 800*1000;
  }

  @Override
  protected int getPAGE_SIZE () {
    return 1000;
  }
}
  