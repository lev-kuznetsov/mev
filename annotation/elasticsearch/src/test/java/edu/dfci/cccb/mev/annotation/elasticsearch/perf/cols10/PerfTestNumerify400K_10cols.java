package edu.dfci.cccb.mev.annotation.elasticsearch.perf.cols10;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;

public class PerfTestNumerify400K_10cols extends AbstractPerfTestNumerify {

//run 1
//  samples: 3
//  max:     14411
//  average: 13796.0
//  median:  13391
  
  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 400*1000;
  }

  @Override
  protected int getPAGE_SIZE () {
    return 1000;
  }
}
  