package edu.dfci.cccb.mev.topgo.domain;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;

public class TopGo extends AbstractAnalysis<TopGo> {

  private Collection<TopGoRow> table;

  @JsonCreator
  private TopGo (Collection<TopGoRow> table) {
    this.table = table;
  }

  @JsonValue
  private Collection<TopGoRow> table () {
    return table;
  }

  @Override
  @JsonProperty
  public String name () {
    return super.name ();
  }
}
