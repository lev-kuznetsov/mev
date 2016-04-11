package edu.dfci.cccb.mev.pe.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PathwayEnrichmentEntry {

  private @JsonProperty ("ID") String id;
  private @JsonProperty ("Description") String description;
  private @JsonProperty ("GeneRatio") String geneRatio;
  private @JsonProperty ("BgRatio") String bgRatio;
  private @JsonProperty ("pvalue") double pValue;
  private @JsonProperty ("p.adjust") double pAdjust;
  private @JsonProperty ("qvalue") double qValue;
  private @JsonProperty ("geneID") String geneId;
  private @JsonProperty ("Count") int count;
}
