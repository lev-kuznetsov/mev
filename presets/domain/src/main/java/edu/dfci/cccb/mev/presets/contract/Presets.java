package edu.dfci.cccb.mev.presets.contract;

import java.util.List;

public interface Presets {
  List<String> list();
  List<Preset> getAll();
  Preset get(String name) throws PresetNotFoundException;
  void put(Preset preset);
  void remove(String name) throws PresetNotFoundException;
}
