package edu.dfci.cccb.mev.pca.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;

public class Pca extends AbstractAnalysis<Pca> {

  private @JsonProperty double[] sdev;
  private @JsonProperty boolean scale;
  private @JsonProperty Map<String, Double> center;
  private @JsonProperty Map<String, Map<String, Double>> x;
  private @JsonProperty Map<String, Map<String, Double>> rotation;
}
