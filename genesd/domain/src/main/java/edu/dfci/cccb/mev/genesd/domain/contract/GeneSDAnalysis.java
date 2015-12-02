package edu.dfci.cccb.mev.genesd.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.genesd
.domain.contract.GeneSDResult;


public interface GeneSDAnalysis extends Analysis {
  public static final String ANALYSIS_TYPE = "Gene SD Analysis";
  public GeneSDResult result(); 
}
