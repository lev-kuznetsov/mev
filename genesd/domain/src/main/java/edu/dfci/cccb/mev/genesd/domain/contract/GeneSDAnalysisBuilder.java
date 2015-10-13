package edu.dfci.cccb.mev.genesd.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder;
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDAnalysis;
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDAnalysisBuilder;


public interface GeneSDAnalysisBuilder extends AnalysisBuilder<AnalysisBuilder<GeneSDAnalysisBuilder,GeneSDAnalysis>, Analysis>   {
  public AnalysisBuilder<GeneSDAnalysisBuilder, GeneSDAnalysis> name(String name); 
}
