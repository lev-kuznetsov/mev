package edu.dfci.cccb.mev.topgo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopGoRow {

  private @JsonProperty String goId;
  private @JsonProperty String goTerm;
  private @JsonProperty String annotatedGenes;
  private @JsonProperty String significantGenes;
  private @JsonProperty String expected;
  private @JsonProperty double pValue;
}
