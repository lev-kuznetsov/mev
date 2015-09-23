package edu.dfci.cccb.mev.histogram.domain.contract;

import java.util.List;

public interface HistogramResult {
  public List<Integer> breaks();
  public List<Integer> counts();
  public List<Double> density();
  public List<Double> mids();  
    
}
