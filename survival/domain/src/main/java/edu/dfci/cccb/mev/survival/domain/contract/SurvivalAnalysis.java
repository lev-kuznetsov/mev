package edu.dfci.cccb.mev.survival.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;


public interface SurvivalAnalysis extends Analysis {
  public SurvivalParams params();
  public SurvivalResult result(); 
}
