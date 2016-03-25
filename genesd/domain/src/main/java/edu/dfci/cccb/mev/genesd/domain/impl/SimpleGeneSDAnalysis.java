package edu.dfci.cccb.mev.genesd.domain.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDAnalysis;
import edu.dfci.cccb.mev.genesd.domain.impl.SimpleGeneSDAnalysis;

@ToString
@Accessors (fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class SimpleGeneSDAnalysis extends AbstractAnalysis<SimpleGeneSDAnalysis> implements GeneSDAnalysis {
  
  public @Getter @JsonProperty String name;
  public @Getter @JsonProperty String type;
  public @Getter @JsonProperty SimpleGeneSDResult result;
}
