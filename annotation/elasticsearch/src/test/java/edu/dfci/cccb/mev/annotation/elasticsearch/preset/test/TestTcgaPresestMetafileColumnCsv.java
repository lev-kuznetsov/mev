package edu.dfci.cccb.mev.annotation.elasticsearch.preset.test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.PresetsImportAppConfiguration;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetNotFoundException;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={PresetsImportAppConfiguration.class})
public class TestTcgaPresestMetafileColumnCsv {

  @Inject Presets presets;

  @Test
  public void testDescriptor () throws PresetNotFoundException, MalformedURLException {
    log.debug ("****PREEEE****"+presets.getAll ().get (0));
    assertThat (presets.get ("OV.HT_HG-U133A.Level_2.tsv").descriptor ().columnUrl (), 
                is(new URL("file:////home/antony/mev/data/tcga/clinical/OV/clinical/OV.clinical_annotations.tsv")));
  }
  
}
