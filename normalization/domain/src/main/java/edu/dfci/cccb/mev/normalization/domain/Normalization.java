package edu.dfci.cccb.mev.normalization.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;

public class Normalization extends AbstractAnalysis<Normalization> {

  private final @JsonProperty String exportName;
  
  public Normalization (String exportName) {
    this.type ("normalization");
    this.exportName = exportName;
  }
}
