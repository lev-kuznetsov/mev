package edu.dfci.cccb.mev.presets.dal;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import lombok.Setter;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;

public class FsPresetValuesLoader implements PresetValuesLoader {

  @Setter @Inject private DatasetBuilder datasetBuilder;
  
  //replace mulitple "/" in the URL with  a single "/"
  @SuppressWarnings("unused")
  private String formatFilePath(String filePath){
    return filePath.replaceFirst ("[/]{2,}", "/");
 }
  
  public FsPresetValuesLoader(DatasetBuilder datasetBuilder){
    this.datasetBuilder=datasetBuilder;
  }
  
  @Override
  public void loadAll (Presets presets) throws PresetException {
    // TODO Auto-generated method stub

  }

  @Override
  public void load (Preset preset) throws PresetException {
    
      try {
        RawInput rawInput = new UrlTsvInput (preset.descriptor ().dataUrl ());
        datasetBuilder.setValueStoreBuilder (new FlatFileValueStoreBuilder ());
        datasetBuilder.build (rawInput);
      } catch (DatasetBuilderException e) {
        throw new PresetException ("Error creating dataset from preset: "+preset, e);
      } catch (InvalidDatasetNameException e) {
        throw new PresetException ("Invalid dataset name while loading preset: "+preset, e);
      } catch (InvalidDimensionTypeException e) {
        throw new PresetException ("Invalid dimension type while loading preset: "+preset, e);
      } catch (IOException e) {
        throw new PresetException ("IO Exception while loading preset: "+preset, e);
      }
  }

  @Override
  public void load (URL url) throws PresetException {
    // TODO Auto-generated method stub

  }

  @Override
  public void load (PresetDescriptor descriptor) throws PresetException {
    // TODO Auto-generated method stub

  }

}
