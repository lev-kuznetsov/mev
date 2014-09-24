package edu.dfci.cccb.mev.annotation.elasticsearch.perf.alltypes;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;

public class PerfTestNumerify_AllTypes_100Krows_10cols extends PerfTestNumerify_AllTypes_Base {

// run 1 each index about 45M
//  samples: 3
//  max:     17080
//  average: 14201.333333333334
//  median:  10171
  
  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 100*1000;
  }

}
