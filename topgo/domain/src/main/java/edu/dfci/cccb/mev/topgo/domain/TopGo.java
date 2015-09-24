package edu.dfci.cccb.mev.topgo.domain;

import java.util.Collection;

import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;

@ToString
public class TopGo extends AbstractAnalysis<TopGo> {

  private Collection<TopGoRow> table;

  @JsonCreator
  private TopGo (Collection<TopGoRow> table) {
    this.table = table;
  }

  @JsonProperty ("results")
  private Collection<TopGoRow> table () {
    return table;
  }

  @Override
  @JsonProperty
  public String name () {
    return super.name ();
  }
  
  @Override
  @JsonProperty
  public String type() {
    return super.type ();
  }
}
