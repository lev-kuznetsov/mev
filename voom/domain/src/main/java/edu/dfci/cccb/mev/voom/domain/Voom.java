package edu.dfci.cccb.mev.voom.domain;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;

public class Voom extends AbstractAnalysis<Voom> {

  private @JsonProperty ("results") Collection<VoomEntry> table;

  @JsonCreator
  public Voom (Collection<VoomEntry> table) {
    this.table = table;
    type ("voom");
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
