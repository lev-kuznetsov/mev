package edu.dfci.cccb.mev.histogram.domain.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramAnalysis;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramResult;

@ToString
@Accessors (fluent = true)
@RequiredArgsConstructor
public class SimpleHistogramAnalysis extends AbstractAnalysis<SimpleHistogramAnalysis> implements HistogramAnalysis {
  public final @Getter @JsonProperty String name;
  public final @Getter @JsonProperty String type;
  public final @Getter @JsonProperty HistogramResult result;
}
