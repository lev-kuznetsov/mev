package edu.dfci.cccb.mev.genesd.domain.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDAnalysis;
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDResult;
import edu.dfci.cccb.mev.genesd.domain.impl.SimpleGeneSDAnalysis;

@ToString
@Accessors (fluent = true)
@RequiredArgsConstructor
public class SimpleGeneSDAnalysis extends AbstractAnalysis<SimpleGeneSDAnalysis> implements GeneSDAnalysis {
  public final @Getter @JsonProperty String name;
  public final @Getter @JsonProperty String type;
  public final @Getter @JsonProperty GeneSDResult result;
}
