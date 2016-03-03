package edu.dfci.cccb.mev.pca.domain;

import java.util.Map;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;

@Accessors(fluent=true)
public class Pca extends AbstractAnalysis<Pca> {

  private @Getter @JsonProperty double[] sdev;
//  private @JsonProperty boolean scale;
//  private @JsonProperty Map<String, Double> center;
  private @Getter @JsonProperty Map<String, Map<String, Double>> x;
//  private @JsonProperty Map<String, Map<String, Double>> rotation;
}
