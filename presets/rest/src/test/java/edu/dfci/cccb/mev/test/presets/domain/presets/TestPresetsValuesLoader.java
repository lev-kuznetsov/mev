package edu.dfci.cccb.mev.test.presets.domain.presets;

import javax.inject.Inject;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetNotFoundException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;

@ContextConfiguration(classes={PresetsRestConfigurationTest.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPresetsValuesLoader {

  @Inject PresetValuesLoader loader;
  @Inject Presets presets;
  
  @Test @Ignore
  public void testLoadAll () throws PresetException {
    loader.loadAll (presets);
  }

  @Test @Ignore
  public void testLoadOne () throws PresetException, PresetNotFoundException {
    Preset preset = presets.get("UNC_KIRP_AgilentG4502A_07_3_l3");
    loader.load (preset);
  }

}
