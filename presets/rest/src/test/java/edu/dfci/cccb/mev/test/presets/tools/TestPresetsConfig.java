package edu.dfci.cccb.mev.test.presets.tools;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetDomainBuildersConfiguration;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;
import edu.dfci.cccb.mev.presets.contract.Presets;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DatasetDomainBuildersConfiguration.class, PresetsRestToolConfiguration.class, ProbeAnnotationsPersistanceConfigTool.class, RDispatcherConfiguration.class})
public class TestPresetsConfig {

  private @Inject Presets presets;
  private @Inject @Named("presets-datasource") DataSource dataSource;
  @Inject Environment environment;
  @Inject @Named("presets-config") Config config;
  @Inject @Named("rserve.config") Config rserveConfig;
  
  @Test  @Ignore
  //this test only works in local profile
  public void test () {
        log.debug (String.format("env: %s\ncnf: %s", environment.getProperty ("MEV_HOME"), config.getProperty ("MEV_HOME")));
        log.debug (String.format("env: %s\ncnf: %s", environment.getProperty ("mev.annotations.probe.root.metadata.file"), config.getProperty ("mev.annotations.probe.root.metadata.file")));
        log.debug (String.format("rdispatcher config  hosts: %s", rserveConfig.getStringArray ("rserve.host", "localhost:5555")));
        
  }

}
