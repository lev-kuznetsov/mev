package edu.dfci.cccb.mev.genemad.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADResult;


public interface GeneMADAnalysis extends Analysis {  
  public GeneMADResult result(); 
}
