package edu.dfci.cccb.mev.pe.domain;

import static java.util.Calendar.getInstance;

import java.util.ArrayList;
import java.util.Calendar;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;

@Accessors (fluent = true)
public class PathwayEnrichment extends ArrayList<EnrichmentEntry> implements Analysis {
  private static final long serialVersionUID = 1L;

  private @JsonProperty @Getter @Setter String name;
  private @JsonProperty @Getter @Setter (AccessLevel.PRIVATE) String type;
  private @JsonProperty @Getter @Setter String status = Analysis.MEV_ANALYSIS_STATUS_COMPLETED;
  private @JsonProperty @Getter @Setter String error;
  private @Getter Calendar timestamp = getInstance ();

  public PathwayEnrichment () {
    type ("pe");
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object) */
  @Override
  public int compareTo (Analysis o) {
    return timestamp ().compareTo (o.timestamp ());
  }
}
