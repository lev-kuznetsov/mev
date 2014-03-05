package edu.dfci.cccb.mev.dataset.domain.contract;


public interface DimensionBuilder {
  Dimension build(Dimension.Type type);
}
