package edu.dfci.cccb.mev.survival.domain.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalInputEntry;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
public class SimpleSurvivalInputEntry implements SurvivalInputEntry {

  @Getter @JsonProperty private String key;
  @Getter @JsonProperty private Long time;
  @Getter @JsonProperty private Integer status;
  @Getter @JsonProperty private Integer group;    
}
