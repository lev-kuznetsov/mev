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

public class PerfTestNumerify_AllTypes_10rows_10cols extends PerfTestNumerify_AllTypes_Base {

// run 1

  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 10;
  }

}
