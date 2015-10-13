package edu.dfci.cccb.mev.genemad.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADAnalysis;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADAnalysisBuilder;


public interface GeneMADAnalysisBuilder extends AnalysisBuilder<AnalysisBuilder<GeneMADAnalysisBuilder,GeneMADAnalysis>, Analysis>   {
  public AnalysisBuilder<GeneMADAnalysisBuilder, GeneMADAnalysis> name(String name); 
}
