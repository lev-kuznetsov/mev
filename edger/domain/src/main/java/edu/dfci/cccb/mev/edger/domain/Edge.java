package edu.dfci.cccb.mev.edger.domain;

import java.util.Collection;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@NoArgsConstructor
public class Edge extends AbstractAnalysis<Edge> {

  private @JsonProperty @Getter Collection<EdgeEntry> results;
  private @JsonProperty @Getter EdgeParams params;
  public Edge (EdgeParams params, Collection<EdgeEntry> results) {
    this.params = params;
    this.results = results;
    type ("edger");
  }

  @Override
  @JsonProperty
  public String name () {
    return super.name ();
  }

  @Override
  @JsonProperty
  public String type () {
    return super.type ();
  }

  @Accessors(fluent = true)
  @NoArgsConstructor
  @AllArgsConstructor
  public static class EdgeParams {
    @JsonProperty @Getter String name;
    @JsonProperty @Getter Selection experiment;
    @JsonProperty @Getter Selection control;
    @JsonProperty @Getter String method; // one of: "fdr", "holm", "hochberg", "BH", "BY", "bonferroni", "none"
  }
}
