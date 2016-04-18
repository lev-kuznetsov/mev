package edu.dfci.cccb.mev.edger.domain;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;

public class Edge extends AbstractAnalysis<Edge> {

  private @JsonProperty Collection<EdgeEntry> results;

  public Edge (Collection<EdgeEntry> results) {
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
}
