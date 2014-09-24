package edu.dfci.cccb.mev.annotation.elasticsearch.perf.notindexed;

import static org.junit.Assert.*;

import java.io.IOException;

import lombok.extern.log4j.Log4j;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.timer.ConstantTimer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder_NoIndex;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

@Log4j
public class PerfTestNumerify_NoIndex_06Krows_500cols extends PerfTestNumerify_NoIndex_Base {

// run 1
//  samples: 3
//  max:     4413
//  average: 3457.3333333333335
//  median:  2611
  
  
  @Override
  protected long getNUMCOLS () {
    return 500;
  }

  @Override
  protected int getNUMROWS () {
    return 6000;
  }

}
