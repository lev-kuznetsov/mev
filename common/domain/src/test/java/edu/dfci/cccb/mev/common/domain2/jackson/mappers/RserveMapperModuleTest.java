package edu.dfci.cccb.mev.common.domain2.jackson.mappers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.common.domain2.annotation.Rserve;
import edu.dfci.cccb.mev.common.domain2.jackson.guice.RserveMapperModule;

@RunWith (JukitoRunner.class)
public class RserveMapperModuleTest {

  public static class RserveTestModule extends JukitoModule {
    protected void configureTest () {
      install (new RserveMapperModule ());
    }
  }

  @Test
  public void writeDoubleInfinity (@Rserve ObjectMapper m) throws Exception {
    assertThat (m.writeValueAsString (Double.POSITIVE_INFINITY), is ("\"Inf\""));
  }
}
