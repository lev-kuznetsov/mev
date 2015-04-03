package edu.dfci.cccb.mev.survival.domain.impl;

import lombok.Setter;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalAnalysisBuilder;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalParams;
import edu.dfci.cccb.mev.survival.domain.prototype.AbstractSurvivalAnalysisBuilder;

public class SimpleSurvivalAnalysisBuilder extends AbstractDispatchedRAnalysisBuilder<SimpleSurvivalAnalysisBuilder, SimpleSurvivalAnalysis> {
    
  public SimpleSurvivalAnalysisBuilder () {
    super ("Survival Analysis");
  }

  @Parameter private SurvivalParams params;  
  public SimpleSurvivalAnalysisBuilder params (SurvivalParams params) {
    this.params=params;
    return this;
  }
  
  @Result private SimpleSurvivalResult dtoResult;
  
  @Override @JsonProperty
  protected SimpleSurvivalAnalysis result () {
    // TODO Auto-generated method stub
    return new SimpleSurvivalAnalysis (params.name(), this.type(), params, dtoResult);
  }
}
