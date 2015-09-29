package edu.dfci.cccb.mev.genemad.domain.prototype;

import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADAnalysis;

public abstract class AbstractGeneMADAnalysisBuilder 
extends AbstractDispatchedRAnalysisBuilder<AbstractGeneMADAnalysisBuilder, GeneMADAnalysis>{

  public AbstractGeneMADAnalysisBuilder () {
    super ("Histogram Analysis");
  }

}
