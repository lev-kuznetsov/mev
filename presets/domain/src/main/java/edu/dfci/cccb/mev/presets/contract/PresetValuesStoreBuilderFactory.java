package edu.dfci.cccb.mev.presets.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;

public interface PresetValuesStoreBuilderFactory {
  ValueStoreBuilder create(String id) throws PresetException;
}
