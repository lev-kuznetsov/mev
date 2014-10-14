package edu.dfci.cccb.mev.annotation.elasticsearch.perf.rows6K;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;

public class PerfTestNumerify_06Krows_1000cols extends AbstractPerfTestNumerify {

// run 1
//  samples: 3 maxheap: 2g
//  max:     43437
//  average: 39158.0
//  median:  33534
// run 2
//  samples: 3 maxheap: 4g
//  max:     327861
//  average: 237172.66666666666
//  median:  164301
// run 3
//  samples: 1
//  max:     108516
//  average: 108516.0
//  median:  108516
// run 4 G1 collector maxheap 4g
//  samples: 3
//  max:     106581
//  average: 97930.33333333333
//  median:  83883
// run 5 G1 collector maxheap 4g
//  samples: 3
//  max:     187907
//  average: 91417.33333333333
//  median:  20573
  
  @Override
  protected long getNUMCOLS () {
    return 1000;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

}
