package edu.dfci.cccb.mev.histogram.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder;


public interface HistogramAnalysisBuilder extends AnalysisBuilder<AnalysisBuilder<HistogramAnalysisBuilder,HistogramAnalysis>, Analysis>   {
  public AnalysisBuilder<HistogramAnalysisBuilder, HistogramAnalysis> name(String name); 
}
