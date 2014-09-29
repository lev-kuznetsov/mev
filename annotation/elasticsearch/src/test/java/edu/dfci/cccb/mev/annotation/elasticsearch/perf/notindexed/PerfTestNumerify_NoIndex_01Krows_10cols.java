package edu.dfci.cccb.mev.annotation.elasticsearch.perf.notindexed;

public class PerfTestNumerify_NoIndex_01Krows_10cols extends PerfTestNumerify_NoIndex_Base {
  
  
  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 1000;
  }

}
