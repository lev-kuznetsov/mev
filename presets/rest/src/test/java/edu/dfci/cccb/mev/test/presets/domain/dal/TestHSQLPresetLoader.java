package edu.dfci.cccb.mev.test.presets.domain.dal;

import static org.junit.Assert.*;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetNotFoundException;
import edu.dfci.cccb.mev.presets.dal.HSQLPresetLoader;
import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;

@ContextConfiguration(classes={PresetsRestConfigurationTest.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestHSQLPresetLoader {

  @Inject Presets presets;
  @Inject @Named("presets-datasource") DataSource dataSource;
  
  @Test @Ignore
  public void testLoadURL () throws PresetException, PresetNotFoundException {
    HSQLPresetLoader loader = new HSQLPresetLoader (dataSource, true, 1000);
    loader.load (presets.get (presets.list ().get(0)));
  }
}
