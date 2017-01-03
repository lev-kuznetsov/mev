package edu.dfci.cccb.mev.presets.tcga;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetsBuilder;
import edu.dfci.cccb.mev.presets.prototype.AbstractTcgaPreset;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public class TcgaPresetsBuilderJson extends AbstractPresetsBuilder {



  @SuppressWarnings("unchecked")
  static <T> Class<? extends T[]> getArrayClass(Class<T> clazz) {
    return (Class<? extends T[]>) Array.newInstance(clazz, 0).getClass();
  }

  public TcgaPresetsBuilderJson(Class<? extends AbstractTcgaPreset> tcgaPresetClass) {
    super(tcgaPresetClass);
  }

  @Override
  @SneakyThrows({IOException.class})
  public List<Preset> getAll(URL data) throws PresetException{
    ObjectMapper mapper = new ObjectMapper();
    List<Preset> result = new ArrayList<Preset>();
    Class<? extends ATcgaPresetEntry[]> tcgaPresetEntryArrayClass = getArrayClass(tcgaPresetEntryClass());
    for(ATcgaPresetEntry entry : mapper.readValue(data, tcgaPresetEntryArrayClass)){
      result.add(createPreset (entry.formatPreset()));
    }
    return result;
  }
}
