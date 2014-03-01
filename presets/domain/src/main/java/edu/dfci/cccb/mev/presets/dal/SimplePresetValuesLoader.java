package edu.dfci.cccb.mev.presets.dal;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;

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
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
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
      load(preset);
    }    
  }
  
  public void load(Preset preset) throws PresetException{    
        log.info ("*** loading Preset DATASET:"+preset.name ()+" data:"+preset.descriptor ().dataUrl ());  
        load(preset.name (), preset.descriptor ().dataUrl ());      
  }
  
  public void load(PresetDescriptor descriptor) throws PresetException{    
    log.info ("*** loading Preset DATASET:"+descriptor.name ()+" data:"+descriptor.dataUrl ());  
    load(descriptor.name (), descriptor.dataUrl ());      
}
  
  public void load(URL url) throws PresetException{    
      String tableName = FilenameUtils.getName (url.getPath ());
      load(tableName, url);    
  }
  
  public void load(String name, URL url) throws PresetException{
    try{
      log.info ("*** loading Preset DATASET URL:"+ url);  
      RawInput rawInput= new UrlTsvInput (url); 
      ValueStoreBuilder valueStoreBuilder = valueStoreBuilderInjector.create (name);
      datasetBuilder.setValueStoreBuilder (valueStoreBuilder);
      datasetBuilder.build (rawInput);
    }catch(DatasetBuilderException | InvalidDatasetNameException | InvalidDimensionTypeException e){
      throw new PresetException ("Error while building preset dataset name: " + url, e);
    }
  }
}
