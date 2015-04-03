package edu.dfci.cccb.mev.survival.domain.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalInputEntryTcga;

@ToString
@NoArgsConstructor
@Accessors(fluent=true)
public class SimpleSurvivalInputEntryTcga extends SimpleSurvivalInputEntry implements SurvivalInputEntryTcga {

  public SimpleSurvivalInputEntryTcga(String key, Long time, Integer status, Integer group, Long days_to_death, Long days_to_last_followup, String vital_status){
    super(key, time, status, group);
    this.days_to_death=days_to_death;
    this.days_to_last_followup=days_to_last_followup;
    this.vital_status=vital_status;
  }
  
  @Getter @JsonProperty private Long days_to_death;
  @Getter @JsonProperty private Long days_to_last_followup;
  @Getter @JsonProperty private String vital_status;  
  
}
