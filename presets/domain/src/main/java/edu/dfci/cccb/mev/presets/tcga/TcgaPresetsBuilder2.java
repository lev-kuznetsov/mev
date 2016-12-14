package edu.dfci.cccb.mev.presets.tcga;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public class TcgaPresetsBuilder2 extends TcgaPresetsBuilder  {

  @Inject @Named("tcgaPreset2") @Getter Provider<Preset> tcgaPresetProvider;
  private  ObjectMapper mapper;
  private  Class<? extends ATcgaPresetEntry[]> tcgaPresetEntryArrayClass;

  @SuppressWarnings("unchecked")
  static <T> Class<? extends T[]> getArrayClass(Class<T> clazz) {
    return (Class<? extends T[]>) Array.newInstance(clazz, 0).getClass();
  }

  public TcgaPresetsBuilder2(Class<? extends ATcgaPresetEntry> tcgaPresetEntryClass) {
    super();
    this.tcgaPresetEntryArrayClass = getArrayClass(tcgaPresetEntryClass);
  }

  @PostConstruct
  private void init(){
    if(this.mapper==null)
      this.mapper = new ObjectMapper();
  }

  @Override
  @SneakyThrows({IOException.class})
  public List<Preset> getAll(URL data) throws PresetException{

    if(this.mapper==null)
      this.init();

    List<Preset> result = new ArrayList<Preset>();

    for(ATcgaPresetEntry entry : mapper.readValue(data, this.tcgaPresetEntryArrayClass)){
      result.add(createPreset (entry.formatPreset()));
    }

    return result;
  }
}
