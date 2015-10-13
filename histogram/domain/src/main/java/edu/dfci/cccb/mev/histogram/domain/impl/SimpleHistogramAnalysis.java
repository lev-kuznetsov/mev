package edu.dfci.cccb.mev.histogram.domain.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramAnalysis;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramResult;

@ToString
@Accessors (fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class SimpleHistogramAnalysis extends AbstractAnalysis<SimpleHistogramAnalysis> implements HistogramAnalysis {
  public @Getter @JsonProperty String name;
  public @Getter @JsonProperty String type;
  public @Getter @JsonProperty SimpleHistogramResult result;
}
