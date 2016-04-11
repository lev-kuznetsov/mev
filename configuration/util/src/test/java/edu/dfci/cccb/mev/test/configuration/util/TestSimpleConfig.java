package edu.dfci.cccb.mev.test.configuration.util;

import lombok.extern.log4j.Log4j;

import org.junit.Ignore;
import org.junit.Test;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.configuration.util.simple.SimpleConfig;

@Log4j
public class TestSimpleConfig {

  @Test @Ignore
  public void testSimpleConfigString () {
    Config config = new SimpleConfig("simple.properties");
  }

  @Test
  public void testGetProfiles() {
    Config config = new SimpleConfig("simple.properties");
    log.debug(String.format("Profiles: %s", config.getProfiles ()));
  }

}
