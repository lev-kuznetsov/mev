package edu.dfci.cccb.mev.topgo.domain;

import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

@ToString
public class TopGoRow {

  private @JsonProperty String goId;
  private @JsonProperty String goTerm;
  private @JsonProperty Integer annotatedGenes;
  private @JsonProperty Integer significantGenes;
  private @JsonProperty String expected;
  private @JsonProperty double pValue;
  private @JsonProperty ("adj.p") double adjP;
}
