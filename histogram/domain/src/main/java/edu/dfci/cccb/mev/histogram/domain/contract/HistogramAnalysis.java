package edu.dfci.cccb.mev.histogram.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;


public interface HistogramAnalysis extends Analysis {  
  public HistogramResult result(); 
}
