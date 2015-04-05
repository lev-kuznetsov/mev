package edu.dfci.cccb.mev.survival.domain.contract;

import java.util.List;

import edu.dfci.cccb.mev.dataset.domain.contract.Selection;

public interface SurvivalParams {
  public String name();
  public String datasetName();
  public String experimentName();
  public Selection experiment();
  public String controlName();
  public Selection control();
  public List<SurvivalInputEntryTcga> input();  
}
