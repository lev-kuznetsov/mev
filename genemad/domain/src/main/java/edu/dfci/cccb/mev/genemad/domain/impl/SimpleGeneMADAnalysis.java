package edu.dfci.cccb.mev.genemad.domain.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADAnalysis;
import edu.dfci.cccb.mev.genemad.domain.impl.SimpleGeneMADAnalysis;

@ToString
@Accessors (fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class SimpleGeneMADAnalysis extends AbstractAnalysis<SimpleGeneMADAnalysis> implements GeneMADAnalysis {
  public @Getter @JsonProperty String name;
  public @Getter @JsonProperty String type;
  public @Getter @JsonProperty SimpleGeneMADResult result;
}
