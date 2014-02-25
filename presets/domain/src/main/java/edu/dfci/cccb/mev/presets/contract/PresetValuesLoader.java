package edu.dfci.cccb.mev.presets.contract;

import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;

public interface PresetValuesLoader {
  void loadAll(Presets presets) throws PresetException;
}
