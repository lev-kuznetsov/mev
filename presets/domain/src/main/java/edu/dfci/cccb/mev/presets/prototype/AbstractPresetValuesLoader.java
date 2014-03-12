package edu.dfci.cccb.mev.presets.prototype;

import java.net.URL;

import org.apache.commons.io.FilenameUtils;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;

public abstract class AbstractPresetValuesLoader implements PresetValuesLoader {

  @Override
  public void loadAll (Presets presets) throws PresetException {
    for(Preset preset : presets.getAll ())
      load(preset);
  }

  @Override
  public void load (Preset preset) throws PresetException {
    load(preset.descriptor ().dataUrl ());  
  }

  @Override
  public abstract void load (URL url) throws PresetException;

  @Override
  public void load (PresetDescriptor descriptor) throws PresetException {
    load(descriptor.dataUrl ());    
  }
  
  protected String getTableName(URL url){
    return getTableNamePrefix()+FilenameUtils.getName (url.getPath ());
  }
 
  protected String getTableNamePrefix(){
    return "";
  }
}
