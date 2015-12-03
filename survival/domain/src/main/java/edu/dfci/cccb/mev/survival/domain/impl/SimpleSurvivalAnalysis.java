package edu.dfci.cccb.mev.survival.domain.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalAnalysis;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalParams;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalResult;

@ToString
@Accessors(fluent=true)
public class SimpleSurvivalAnalysis extends AbstractAnalysis<SimpleSurvivalAnalysis> implements SurvivalAnalysis{  
  public @Getter @JsonProperty SurvivalParams params;
  public @Getter @JsonProperty SurvivalResult result;
  public SimpleSurvivalAnalysis(String name, String type, SurvivalParams params, SurvivalResult result){
    name(name);
    type(type);
    this.params = params;
    this.result = result;    
  }
}
