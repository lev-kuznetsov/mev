package edu.dfci.cccb.mev.survival.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;



public interface SurvivalAnalysisBuilder extends AnalysisBuilder<AnalysisBuilder<?,?>, Analysis>   {
  public SurvivalAnalysisBuilder params(SurvivalParams params); 
}
