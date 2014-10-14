package edu.dfci.cccb.mev.annotation.elasticsearch.preset;

import edu.dfci.cccb.mev.presets.tcga.TcgaPresetMetafile;

public class TcgaPresestMetafileColumnCsv extends TcgaPresetMetafile {
  @Override
  protected String getColumnUrlSpec () {
    return "clinical/"+disease()+"/clinical/"+disease()+".clinical_annotations.tsv";
  }
}
