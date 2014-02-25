package edu.dfci.cccb.mev.test;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.inject.Named;
import javax.inject.Provider;

import lombok.extern.log4j.Log4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.server.configuration.MyBeanClass;
import edu.dfci.cccb.mev.annotation.server.configuration.MyTest;
import edu.dfci.cccb.mev.test.annotation.domain.probe.dal.jooq.TestJooqProbeAnnotations;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MyTest.class})
public class TestMyBean {

  private @Autowired Collection<MyBeanClass> lol;
  
  @Test
  public void test () {
    log.warn (lol.size ());
  }

}
