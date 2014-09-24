package edu.dfci.cccb.mev.annotation.elasticsearch.perf.cols10;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;

public class PerfTestNumerify_20Krows_10cols extends AbstractPerfTestNumerify {

  @Override
  protected long getNUMCOLS () {
    return 10;
  }
  
//  max:     6155
//  average: 5053.666666666667
//  median:  4458
  @Override
  protected int getNUMROWS () {
    return 20*1000;
  }

}
