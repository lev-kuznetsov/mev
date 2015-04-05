package edu.dfci.cccb.mev.survival.domain.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalParams;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalResult;

@ToString
@Accessors(fluent=true)
@RequiredArgsConstructor
public class SimpleSurvivalAnalysis extends AbstractAnalysis<SimpleSurvivalAnalysis>{  
  public final @Getter @JsonProperty String name;
  public final @Getter @JsonProperty String type;
  public final @Getter @JsonProperty SurvivalParams params;
  public final @Getter @JsonProperty SurvivalResult result;
}
