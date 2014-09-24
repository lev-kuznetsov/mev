package edu.dfci.cccb.mev.annotation.elasticsearch.perf.notindexed;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder_NoIndex;

public class PerfTestNumerify_NoIndex_01Krows_10cols extends PerfTestNumerify_NoIndex_Base {
  
  
  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 1000;
  }

}
