package edu.dfci.cccb.mev.pe.domain;

import static java.util.Calendar.getInstance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;

@Accessors (fluent = true)
public class PathwayEnrichment extends AbstractAnalysis<PathwayEnrichment> {
  
  @ToString
  @JsonIgnoreProperties (ignoreUnknown = true)
  public static class PathwayEnrichmentParameters {
    private @JsonProperty @Getter String name;
    private @JsonProperty @Getter String[] genelist;
    private @JsonProperty @Getter String organism;
    private @JsonProperty @Getter String pAdjustMethod;
    private @JsonProperty @Getter int minGSSize;
    private @JsonProperty @Getter double pvalueCutoff;
  }
  
  private @JsonProperty @Getter @Setter PathwayEnrichmentParameters params;
  private @JsonProperty @Getter @Setter List<PathwayEnrichmentEntry> result;
  
  
  public PathwayEnrichment () {    
    type ("pe");
  }
  
}
