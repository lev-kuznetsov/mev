package edu.dfci.cccb.mev.histogram.domain.impl;

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
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramAnalysis;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramAnalysisBuilder;

@R ("function (dataset) {\n"
      + "hist.result=hist(as.matrix(dataset), plot=FALSE);\n"
      + "list(breaks=hist.result$breaks, counts=hist.result$counts, density=hist.result$density, mids=hist.result$mids);\n"
      + "}")
public class SimpleHistogramAnalysisBuilder extends AbstractDispatchedRAnalysisBuilder<SimpleHistogramAnalysisBuilder, HistogramAnalysis>{
  
  public SimpleHistogramAnalysisBuilder () {
    super (HistogramAnalysis.ANALYSIS_TYPE);
  }
  
  @Result private SimpleHistogramResult dtoResult;
  @Error private String error;
  
  @Override
  protected HistogramAnalysis result () {
    return new SimpleHistogramAnalysis (this.name(), this.type(), dtoResult);
  }
  
  @Callback
  private void cb () {
    System.out.println ("result:"+dtoResult);
    System.out.println ("error:"+error);
  }
}
