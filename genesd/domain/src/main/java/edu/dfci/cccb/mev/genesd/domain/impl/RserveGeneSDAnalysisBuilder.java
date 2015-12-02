package edu.dfci.cccb.mev.genesd.domain.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Error;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDAnalysis;
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDAnalysisBuilder;
import edu.dfci.cccb.mev.genesd.domain.impl.SimpleGeneSDAnalysis;
import edu.dfci.cccb.mev.genesd.domain.impl.RserveGeneSDAnalysisBuilder;
import edu.dfci.cccb.mev.genesd.domain.impl.SimpleGeneSDResult;

@R ("function (dataset) {\n"
      + "gene.sd=sort(apply(as.matrix(dataset), 1, sd), decreasing=TRUE)\n" 
      + "list(genes=names(gene.sd), sd=gene.sd)\n"
      + "}")
public class RserveGeneSDAnalysisBuilder extends AbstractDispatchedRAnalysisBuilder<RserveGeneSDAnalysisBuilder, GeneSDAnalysis>{
    
  public RserveGeneSDAnalysisBuilder () {
    super (GeneSDAnalysis.ANALYSIS_TYPE);
  }
  
  @Result private SimpleGeneSDResult dtoResult;
  @Error private String error;
  
  @Override
  protected GeneSDAnalysis result () {
    return new SimpleGeneSDAnalysis (this.name(), this.type(), dtoResult);
  }
  
  @Callback
  private void cb () {
    System.out.println ("result:"+dtoResult);
    System.out.println ("error:"+error);
  }
}
