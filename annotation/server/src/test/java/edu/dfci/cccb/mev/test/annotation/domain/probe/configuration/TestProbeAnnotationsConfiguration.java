package edu.dfci.cccb.mev.test.annotation.domain.probe.configuration;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate4.SpringJtaSessionContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsPersistanceConriguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ProbeAnnotationsPersistanceConriguration.class})
public class TestProbeAnnotationsConfiguration {

  @Inject @Named("probe-annotations-datasource") DataSource dataSource;

  
  @Test
  public void test () {
    assertNotNull (dataSource);
  }

  
}
