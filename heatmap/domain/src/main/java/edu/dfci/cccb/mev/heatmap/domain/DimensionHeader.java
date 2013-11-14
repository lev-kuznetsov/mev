package edu.dfci.cccb.mev.heatmap.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DimensionHeader<T> {
  
  Annotation getAnnotation();
  Dimension getDimension();
  Map<String, DimensionSubset<T>> getKeysets();
  void addKeyset(DimensionSubset<T> keyset);
  Set<String> getKeysetNames ();
  List<DimensionSubset<T>> getKeysetList ();
  
}