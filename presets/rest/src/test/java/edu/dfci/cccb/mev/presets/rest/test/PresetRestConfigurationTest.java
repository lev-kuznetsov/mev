package edu.dfci.cccb.mev.presets.rest.test;

import static org.junit.Assert.*;

import java.net.URL;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={PresetsRestConfiguration.class})
public class PresetRestConfigurationTest {
  
  //@Inject Presets tcgaPresets;
  @Inject URL tcgaPresetsRoot;
  
  @Test @Ignore
  public void testGetTcgaPresets () {
    fail ("Not yet implemented");
  }

  @Test
  public void testTcgaPresetsRoot () {
    //assertEquals("file:/home/antony/work/danafarber/mev/data/tcga/tcga_data_tempSubset", tcgaPresetsRoot.toString ());
    
  }

  @Test
  public void testFoo() {
    //assertEquals("file:/home/antony/work/danafarber/mev/data/tcga/tcga_data_tempSubset", tcgaPresetsRoot.toString ());
    
  }
}
