package edu.dfci.cccb.mev.presets.contract;

import java.net.URL;

import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;

public interface PresetValuesLoader {
  void loadAll(Presets presets) throws PresetException;
  void load(Preset preset) throws PresetException;
  void load(URL url) throws PresetException;
  void load(PresetDescriptor descriptor) throws PresetException;

}
