package edu.dfci.cccb.mev.annotation.elasticsearch.perf.cols100;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;

public class PerfTestNumerify_06Krows_100cols extends AbstractPerfTestNumerify {

//  max:     4713
//  average: 4389.0
//  median:  4097
  
  @Override
  protected long getNUMCOLS () {
    return 100;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

}
