package edu.dfci.cccb.mev.presets.tcga.contract;

import edu.dfci.cccb.mev.presets.contract.Preset;

public interface TcgaPreset extends Preset{

  public abstract String disease ();
  public abstract String diseaseName ();
  public abstract String platform ();
  public abstract String platformName ();

}