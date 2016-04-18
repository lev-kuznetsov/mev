package edu.dfci.cccb.mev.edger.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EdgeEntry {

  private @JsonProperty ("_row") String id;
  private @JsonProperty ("logFC") double logFc;
  private @JsonProperty ("logCPM") double logCpm;
  private @JsonProperty ("PValue") double pValue;
  private @JsonProperty (value = "FWER", required = false) int fwer;
  private @JsonProperty (value = "FDR", required = false) int fdr;
}
