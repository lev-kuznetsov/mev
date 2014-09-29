package edu.dfci.cccb.mev.annotation.elasticsearch.perf.rows6K;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;

public class PerfTestNumerify_06Krows_100cols extends AbstractPerfTestNumerify {

// run 1
//  max:     4739
//  average: 4490.0
//  median:  4297

// run 2
//  samples: 3
//  max:     5521
//  average: 5141.0
//  median:  4666

//** after fix
// run 3
//  samples: 3
//  max:     3432
//  average: 2299.0
//  median:  1730
  
  @Override
  protected long getNUMCOLS () {
    return 100;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

}
