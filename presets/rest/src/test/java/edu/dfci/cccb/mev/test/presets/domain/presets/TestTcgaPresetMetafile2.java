package edu.dfci.cccb.mev.test.presets.domain.presets;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetsBuilder;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={PresetsRestConfigurationTest.class})
public class TestTcgaPresetMetafile2 {
  
  private @Inject @Named ("tcgaPresetBiulder2") PresetsBuilder builder;
  private @Inject @Named ("tcgaPresetRoot") URL tcgaPresetRoot;
  @Inject @Named ("probe-annotations-root") URL rowAnnotationsRoot;
  
  @Test
  public void testTcgaPresetRoot() throws IOException{
    assertEquals("file", tcgaPresetRoot.getProtocol ());
  }
  
  @Test
  public void testGetDescriptor () throws PresetException, MalformedURLException  {
    
    Preset preset = builder.createPreset (new Object[]{"presetname", "filename.tsv", "path/of/file", "NS", "Nothing Serious", "HS", "HiSeq", "Level2", "log"});
     
    PresetDescriptor descriptor = preset.descriptor ();
    
    URL expectedDataURL = new URL(tcgaPresetRoot, "tcga_data/path/of/file/filename.tsv");    
    assertEquals (expectedDataURL, descriptor.dataUrl ());
    
    
    URL expectedColumnURL = new URL(tcgaPresetRoot, "openrefine/clinical/presetname-clinical_annotations-tsv.openrefine.tar.gz");
    assertEquals (expectedColumnURL, descriptor.columnUrl ());
  
    URL expectedRowURL = new URL(rowAnnotationsRoot, "openrefine/HS-na33-annot-out-tsv.google-refine.tar.gz");    
    assertEquals (expectedRowURL, descriptor.rowUrl ());
  
  }
  
}
