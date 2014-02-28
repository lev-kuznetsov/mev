package edu.dfci.cccb.mev.presets.dal;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetValuesStoreBuilderFactory;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetValuesLoader;

@Log4j
public class SimplePresetValuesLoader extends AbstractPresetValuesLoader {

  @Getter @Setter @Inject @Named("presetDetasetBuilder") DatasetBuilder datasetBuilder;
  @Getter @Setter @Inject PresetValuesStoreBuilderFactory valueStoreBuilderInjector;
  
  public SimplePresetValuesLoader (PresetValuesStoreBuilderFactory builderFactory, DatasetBuilder datasetBuilder) {
    this.datasetBuilder=datasetBuilder;
    this.valueStoreBuilderInjector=builderFactory;
  }

  @Override
  public void loadAll (Presets presets) throws PresetException {
    
    for(Preset preset : presets.getAll ()){
      try{
      log.info ("*** loading Preset DATASET:"+preset.name ()+" data:"+preset.descriptor ().dataUrl ());  
      RawInput rawInput= new UrlTsvInput (preset.descriptor().dataUrl ()); 
      ValueStoreBuilder valueStoreBuilder = valueStoreBuilderInjector.create (preset.name ());
      datasetBuilder.setValueStoreBuilder (valueStoreBuilder);
      datasetBuilder.build (rawInput);
      }catch(DatasetBuilderException | InvalidDatasetNameException | InvalidDimensionTypeException e){
        throw new PresetException ("Error while building preset dataset name: " + preset, e);
      }
    }
    
  }
}
