package edu.dfci.cccb.mev.survival.domain.prototype;

import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalAnalysis;

public abstract class AbstractSurvivalAnalysisBuilder extends AbstractDispatchedRAnalysisBuilder<AbstractSurvivalAnalysisBuilder, SurvivalAnalysis>{

  public AbstractSurvivalAnalysisBuilder () {
    super ("Survival Analysis");
  }

}
