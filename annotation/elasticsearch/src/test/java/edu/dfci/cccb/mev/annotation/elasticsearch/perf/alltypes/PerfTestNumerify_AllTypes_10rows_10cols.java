package edu.dfci.cccb.mev.annotation.elasticsearch.perf.alltypes;

public class PerfTestNumerify_AllTypes_10rows_10cols extends PerfTestNumerify_AllTypes_Base {

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
