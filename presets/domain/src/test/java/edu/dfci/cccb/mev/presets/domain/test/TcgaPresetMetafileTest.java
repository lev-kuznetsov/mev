package edu.dfci.cccb.mev.presets.domain.test;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.tcga.TcgaPresetMetafile;

public class TcgaPresetMetafileTest {

  @Test
  public void testGetDescriptor () throws MalformedURLException {
    Preset preset = new TcgaPresetMetafile ("filename.tsv", "path/of/file", "NS", "Nothing Serious", "HS", "HiSeq");
    PresetDescriptor descriptor = preset.getDescriptor ();
    assertEquals ("file:path/of/file/filename.tsv", descriptor.dataUrl ().toString ());
    assertEquals ("file:NS/clinical/NS.clinical_annotations.tsv", descriptor.columnUrl ().toString ());
  }

}
