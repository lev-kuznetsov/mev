package edu.dfci.cccb.mev.gsea.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;

public class Gsea extends AbstractAnalysis<Gsea> {

  private @JsonProperty List<GseaEntry> result;

  public Gsea (String name, List<GseaEntry> result) {
    type ("gsea");
    name (name);
    this.result = result;
  }
}
