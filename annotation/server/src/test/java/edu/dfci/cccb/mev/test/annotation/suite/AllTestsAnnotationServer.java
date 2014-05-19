package edu.dfci.cccb.mev.test.annotation.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.dfci.cccb.mev.test.annotation.domain.probe.configuration.TestProbeAnnotationsConfiguration;
import edu.dfci.cccb.mev.test.annotation.domain.probe.dal.h2.TestH2ProbeAnnotations;
import edu.dfci.cccb.mev.test.annotation.domain.probe.dal.h2.TestH2ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.test.annotation.server.support.jooq.TestJooqUtil;

@RunWith (Suite.class)
@SuiteClasses ({
  TestProbeAnnotationsConfiguration.class,
  TestH2ProbeAnnotationsLoader.class,
  TestH2ProbeAnnotations.class,
  TestJooqUtil.class
  })
public class AllTestsAnnotationServer {

}
