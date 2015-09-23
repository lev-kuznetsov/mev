package edu.dfci.cccb.mev.histogram.domain.prototype;

import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramAnalysis;

public abstract class AbstractSurvivalAnalysisBuilder 
extends AbstractDispatchedRAnalysisBuilder<AbstractSurvivalAnalysisBuilder, HistogramAnalysis>{

  public AbstractSurvivalAnalysisBuilder () {
    super ("Histogram Analysis");
  }

}
