package edu.dfci.cccb.mev.presets.contract;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;


public interface Preset {
  String name();
  PresetDescriptor getDescriptor(); 
  
}
