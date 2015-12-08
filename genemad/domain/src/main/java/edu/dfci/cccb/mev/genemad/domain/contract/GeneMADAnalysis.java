package edu.dfci.cccb.mev.genemad.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADResult;


public interface GeneMADAnalysis extends Analysis {  
  public static final String ANALYSIS_TYPE = "Gene MAD Analysis";
  public GeneMADResult result(); 
}
