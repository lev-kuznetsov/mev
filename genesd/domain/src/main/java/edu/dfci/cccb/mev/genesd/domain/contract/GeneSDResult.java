package edu.dfci.cccb.mev.genesd.domain.contract;

import java.util.List;

public interface GeneSDResult {
  public List<String> genes();
  public List<Double> sd();
}
