package edu.dfci.cccb.mev.annotation.elasticsearch.perf.cols10;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;

public class PerfTestNumerify_20Krows_10cols extends AbstractPerfTestNumerify {

  @Override
  protected long getNUMCOLS () {
    return 10;
  }
  
//  max:     6155
//  average: 5053.666666666667
//  median:  4458
  @Override
  protected int getNUMROWS () {
    return 20*1000;
  }

}
