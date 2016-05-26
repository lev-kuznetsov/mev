package edu.dfci.cccb.mev.presets.tcga;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.dal.TsvReader;
import edu.dfci.cccb.mev.presets.dal.TsvReaderMetaModel;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetsBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TcgaPresetsBuilder extends AbstractPresetsBuilder  {
    
  @Inject @Named("tcgaPreset") Provider<Preset> tcgaPresetProvider;
  
  @Override
  public Preset createPreset (Object[] values) throws PresetException{
    Preset newPreset = tcgaPresetProvider.get ();
    return newPreset.init(values);
  }

  @Override
  public List<Preset> getAll(URL data) throws PresetException{
    TsvReader reader = new TsvReaderMetaModel();
    reader.init (data);

    List<Preset> result = new ArrayList<Preset>();
    for(Object[] row : reader.readAll ())
      result.add(createPreset (row));

    return result;
  }
}
