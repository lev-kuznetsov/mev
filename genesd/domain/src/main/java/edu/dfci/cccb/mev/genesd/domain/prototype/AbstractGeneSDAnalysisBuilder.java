package edu.dfci.cccb.mev.genesd.domain.prototype;

import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDAnalysis;

public abstract class AbstractGeneSDAnalysisBuilder 
extends AbstractDispatchedRAnalysisBuilder<AbstractGeneSDAnalysisBuilder, GeneSDAnalysis>{

  public AbstractGeneSDAnalysisBuilder () {
    super ("Histogram Analysis");
  }

}
