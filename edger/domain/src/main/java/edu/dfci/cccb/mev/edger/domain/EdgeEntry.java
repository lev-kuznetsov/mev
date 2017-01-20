package edu.dfci.cccb.mev.edger.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EdgeEntry {

  private @JsonProperty ("_row") String id;
  private @JsonProperty ("logFC") double logFc;
  private @JsonProperty ("logCPM") double logCpm;
  private @JsonProperty ("PValue") double pValue;
  private @JsonProperty (value = "FWER", required = false) Double fwer;
  private @JsonProperty (value = "FDR", required = false) Double fdr;
}
