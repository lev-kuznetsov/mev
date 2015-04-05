package edu.dfci.cccb.mev.survival.domain.impl;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalPlotEntry;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalResult;

@ToString
@NoArgsConstructor
@Accessors(fluent=true)
public class SimpleSurvivalResult implements SurvivalResult {
  
  private @Getter @JsonProperty Integer n;
  private @Getter @JsonProperty Integer n_event;
  private @Getter @JsonProperty Double coef;
  private @Getter @JsonProperty Double exp_coef;
  private @Getter @JsonProperty Double se_coef;
  private @Getter @JsonProperty Double z;
  private @Getter @JsonProperty Double pValue;
  private @Getter @JsonProperty Double ci_lower;
  private @Getter @JsonProperty Double ci_upper;
  private @Getter @JsonProperty Logrank logrank;
  //@JsonDeserialize(contentAs=SimpleSurvivalPlotEntry.class)
  private @Getter @JsonProperty  Map<String, List<SimpleSurvivalPlotEntry>> plot;
  
  
  @ToString
  @NoArgsConstructor
  @Accessors(fluent=true)
  public static class Logrank{
    private @Getter @JsonProperty Double score;
    private @Getter @JsonProperty Double pValue;
  }
  
  @ToString
  @NoArgsConstructor
  @Accessors(fluent=true)
  public static class SimpleSurvivalPlotEntry implements SurvivalPlotEntry{
    private @Getter @JsonProperty Integer time;
    private @Getter @JsonProperty Double haz;
        
  }
  
}
