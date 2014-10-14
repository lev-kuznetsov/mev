package edu.dfci.cccb.mev.annotation.elasticsearch.perf.alltypes;

public class PerfTestNumerify_AllTypes_06Krows_1000cols extends PerfTestNumerify_AllTypes_Base {

// run 1 Indecies around 400M
//  samples: 1
//  max:     70031
//  average: 70031.0
//  median:  70031
// run 2 
//  samples: 3
//  max:     64972
//  average: 28372.666666666668
//  median:  6428
  
  @Override
  protected long getNUMCOLS () {
    return 1000;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

}
