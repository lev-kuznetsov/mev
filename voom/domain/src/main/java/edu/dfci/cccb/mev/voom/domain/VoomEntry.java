package edu.dfci.cccb.mev.voom.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoomEntry {
  private @JsonProperty ("_row") String id;
  private @JsonProperty double logFC;
  private @JsonProperty ("AveExpr") double aveExpr;
  private @JsonProperty double t;
  private @JsonProperty ("P.Value") double pValue;
  private @JsonProperty ("adj.P.Val") double adjPValue;
  private @JsonProperty ("B") double beta;
}
