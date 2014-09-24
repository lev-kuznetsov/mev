package edu.dfci.cccb.mev.annotation.elasticsearch.perf.cols100;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;

public class PerfTestNumerify_06Krows_100cols extends AbstractPerfTestNumerify {

//  max:     4713
//  average: 4389.0
//  median:  4097
  
  @Override
  protected long getNUMCOLS () {
    return 100;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

}
