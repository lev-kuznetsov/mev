package edu.dfci.cccb.mev.annotation.elasticsearch.perf.notindexed;

public class PerfTestNumerify_NoIndex_06Krows_3000cols extends PerfTestNumerify_NoIndex_Base {

// run 1
//  samples: 3
//  max:     21857
//  average: 20928.0
//  median:  20007
  
// run2 pagesize 1000
//  samples: 3
//  max:     69757
//  average: 35128.0
//  median:  16708
  
//run 3 pagesize 100
//  samples: 3
//  max:     25998
//  average: 25448.0
//  median:  25126
  
//run 4 pagesize 100, bulkSize 15MB
//  samples: 3
//  max:     30432
//  average: 27822.666666666668
//  median:  22764
  
//run 5 pagesize 100, bulkSize 1MB
//  samples: 3
//  max:     21904
//  average: 18300.666666666668
//  median:  14661
  
//run 6 pagesize 100, bulkSize 1MB
//  samples: 3
//  max:     22052
//  average: 20034.666666666668
//  median:  16947
  
//run 7 pagesize 100, bulkSize 15MB
//  samples: 3 
//  max:     33495
//  average: 31559.333333333332
//  median:  29732

//run 8 pagesize 100, bulkSize 5MB
//  samples: 2
//  max:     30743
//  average: 29037.5
//  median:  27332

//run 9 pagesize 100, bullksize 1MB
//  samples: 3
//  max:     20088
//  average: 19419.666666666668
//  median:  18678
//run 10 
//  samples: 10
//  max:     19890
//  average: 15877.8
//  median:  15663
  
  @Override
  protected long getNUMCOLS () {
    return 3000;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

  @Override  
  protected int getPAGE_SIZE () {   
//    return 1000;
    return 100;
  }
}
