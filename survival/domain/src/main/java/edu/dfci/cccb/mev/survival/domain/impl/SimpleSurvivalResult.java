package edu.dfci.cccb.mev.survival.domain.impl;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.survival.domain.contract.SurvivalResult;

@Accessors(fluent=true)
public class SimpleSurvivalResult implements SurvivalResult {
  public @Getter @JsonProperty double coef;
  public @Getter @JsonProperty double expCoef;
  public @Getter @JsonProperty double seCoef;
  public @Getter @JsonProperty double zScore;
  public @Getter @JsonProperty double pValue;
}
