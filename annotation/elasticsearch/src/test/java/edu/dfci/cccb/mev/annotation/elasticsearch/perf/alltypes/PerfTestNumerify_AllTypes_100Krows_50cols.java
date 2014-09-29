package edu.dfci.cccb.mev.annotation.elasticsearch.perf.alltypes;

public class PerfTestNumerify_AllTypes_100Krows_50cols extends PerfTestNumerify_AllTypes_Base {

// run 1 each index rouphly 200M
//  samples: 3
//  max:     81908
//  average: 65398.0
//  median:  55295

  @Override
  protected long getNUMCOLS () {
    return 50;
  }

  @Override
  protected int getNUMROWS () {
    return 100*1000;
  }

}
