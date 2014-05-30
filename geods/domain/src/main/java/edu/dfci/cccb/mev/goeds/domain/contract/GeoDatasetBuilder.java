package edu.dfci.cccb.mev.goeds.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;

public interface GeoDatasetBuilder extends DatasetBuilder {
  public GeoDatasetBuilder setGeoSource(GeoSource source);
}
