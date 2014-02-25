package edu.dfci.cccb.mev.presets.dal;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.jooq.JooqBasedDatasourceValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetValuesStoreBuilderFactory;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetValuesLoader;

public class PresetsValuesLoader extends AbstractPresetValuesLoader {

  @Getter @Setter @Inject PresetValuesStoreBuilderFactory builderFactory;
  
  public PresetsValuesLoader (JooqBasedDatasourceValueStoreBuilder builder) {
    
  }

  @Override
  public void loadAll (Presets presets) throws PresetException {
    for(Preset preset : presets.getAll ()){
      RawInput rawInput= new UrlTsvInput (preset.descriptor().dataUrl ()); 
      ValueStoreBuilder builder = builderFactory.create (preset.name ());
      
    }
    
  }

}
