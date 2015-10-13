package edu.dfci.cccb.mev.genemad.domain.contract;

import java.util.List;

public interface GeneMADResult {
  public List<String> genes();
  public List<Double> mad();
}
