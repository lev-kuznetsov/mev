package edu.dfci.cccb.mev.presets.tcga;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.dal.TsvReader;
import edu.dfci.cccb.mev.presets.dal.TsvReaderMetaModel;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetsBuilder;
import edu.dfci.cccb.mev.presets.prototype.AbstractTcgaPreset;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public class TcgaPresetsBuilder extends AbstractPresetsBuilder  {

  public TcgaPresetsBuilder(Class<? extends AbstractTcgaPreset> tcgaPresetClass) {
    super(tcgaPresetClass);
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
