package edu.dfci.cccb.mev.heatmap.domain;

import java.util.List;

public interface DimensionSubset<T> extends List<T> {
  String getName();
  String getColor();
  String getDescription();
}
