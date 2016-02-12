package edu.dfci.cccb.mev.gsea.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GseaEntry {

  private @JsonProperty ("ID") String id;
  private @JsonProperty ("Description") String description;
  private @JsonProperty int setSize;
  private @JsonProperty double enrichmentScore;
  private @JsonProperty ("NES") double nes;
  private @JsonProperty ("pvalue") double pValue;
  private @JsonProperty ("p.adjust") double pAdjust;
  private @JsonProperty ("qvalues") double qValues;
}
