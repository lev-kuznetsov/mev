package edu.dfci.cccb.mev.annotation.elasticsearch.perf.rows6K;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;

public class PerfTestNumerify_06Krows_050cols extends AbstractPerfTestNumerify {

// run 1
//  samples: 3
//  max:     1696
//  average: 1630.3333333333333
//  median:  1583
  
  @Override
  protected long getNUMCOLS () {
    return 50;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

}
