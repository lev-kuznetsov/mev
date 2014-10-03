package edu.dfci.cccb.mev.annotation.elasticsearch.perf.touch;

public class PerfTestNumerify_Touch_10rows_10cols extends PerfTestNumerify_Touch_Base {

// run 1

  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 10;
  }

}
