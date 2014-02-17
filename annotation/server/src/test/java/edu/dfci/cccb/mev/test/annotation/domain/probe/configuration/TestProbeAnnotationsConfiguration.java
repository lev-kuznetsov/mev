package edu.dfci.cccb.mev.test.annotation.domain.probe.configuration;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsPersistenceConfiguration;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConfigTest.class})
public class TestProbeAnnotationsConfiguration {

  @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  @Inject ProbeAnnotationsLoader loader;
  @Inject ProbeAnnotations annotations;
  
  @Test
  public void test () {    
    assertNotNull (dataSource);
    assertNotNull (loader);
    assertNotNull (annotations);

  }

}
