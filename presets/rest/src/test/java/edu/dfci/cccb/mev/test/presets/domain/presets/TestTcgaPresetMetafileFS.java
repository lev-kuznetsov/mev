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
public class TestTcgaPresetMetafileFS {
  
  private @Inject @Named ("tcgaPresetBiulderFS") PresetsBuilder builder;
  private @Inject @Named ("tcgaPresetRoot") URL tcgaPresetRoot;
  @Inject @Named ("probe-annotations-root") URL rowAnnotationsRoot;
  
  @Test
  public void testTcgaPresetRoot() throws IOException{
    assertEquals("file", tcgaPresetRoot.getProtocol ());
  }
  
  @Test
  public void testGetDescriptor () throws PresetException, MalformedURLException  {
    Preset preset = builder.createPreset (new Object[]{"20150821-COAD-RNAseqGene-median_length_normalized.txt", "20150821-COAD-RNAseqGene-median_length_normalized.txt", "path/of/file", "NS", "Nothing Serious", "HS", "HiSeq", "3", "log"});
     
    PresetDescriptor descriptor = preset.descriptor ();
    
    URL expectedDataURL = new URL(tcgaPresetRoot, "tcga_cleaned_datasets/path/of/file/20150821-COAD-RNAseqGene-median_length_normalized.txt");
    assertEquals (expectedDataURL, descriptor.dataUrl ());
    
    
    URL expectedColumnURL = new URL(tcgaPresetRoot, "openrefine/clinical/tcga_cleaned_datasets/NS-clinical_annotations-tsv.openrefine.tar.gz");
    assertEquals (expectedColumnURL, descriptor.columnUrl ());
  
    URL expectedRowURL = new URL(rowAnnotationsRoot, "openrefine/geneSymbol_goAnnotations-tsv.google-refine.tar.gz");
    assertEquals (expectedRowURL, descriptor.rowUrl ());
  
  }
  
}
