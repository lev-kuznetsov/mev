package edu.dfci.cccb.mev.annotation.elasticsearch.perf.cols10;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;

public class PerfTestNumerify_03Krows_10cols extends AbstractPerfTestNumerify {

  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 3000;
  }

}
