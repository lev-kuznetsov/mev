package edu.dfci.cccb.mev.histogram.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;


public interface HistogramAnalysis extends Analysis {
  public static final String ANALYSIS_TYPE = "Histogram Analysis";
  public HistogramResult result(); 
}
