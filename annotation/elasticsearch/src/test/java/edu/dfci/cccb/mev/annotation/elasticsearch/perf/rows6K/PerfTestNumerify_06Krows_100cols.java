package edu.dfci.cccb.mev.annotation.elasticsearch.perf.rows6K;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;

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
