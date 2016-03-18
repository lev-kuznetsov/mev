package edu.dfci.cccb.mev.genemad.domain.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Error;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADAnalysis;

@Log4j
@R ("function (dataset) {\n"
      + "gene.mad=sort(apply(as.matrix(dataset),1,mad), decreasing=TRUE)\n" 
      + "genemad<-list(genes=names(gene.mad), mad=gene.mad)"
      + "}")
public class RserveGeneMADAnalysisBuilder extends AbstractDispatchedRAnalysisBuilder<RserveGeneMADAnalysisBuilder, GeneMADAnalysis>{
  
  public RserveGeneMADAnalysisBuilder () {
    super ("Gene MAD Analysis");
  }
  
  @Result private SimpleGeneMADResult dtoResult;
  @Error private String error;
  
  @Override
  protected GeneMADAnalysis result () {
    return new SimpleGeneMADAnalysis (this.name(), this.type(), dtoResult);
  }
  
  @Callback
  private void cb () {
	  log.error ("RserveGeneMADAnalysisBuilder ERROR: "+error);
  }
}
