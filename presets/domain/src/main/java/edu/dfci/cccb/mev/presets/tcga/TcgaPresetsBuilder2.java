package edu.dfci.cccb.mev.presets.tcga;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.dal.TsvReader;
import edu.dfci.cccb.mev.presets.dal.TsvReaderMetaModel;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetsBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TcgaPresetsBuilder2 extends TcgaPresetsBuilder  {

  @Inject @Named("tcgaPreset2") Provider<Preset> tcgaPresetProvider;
  private  ObjectMapper mapper;

  protected Object[] formatPreset(TcgaPresetEntry values){
    return new String[]{
            values.location(),   //0: preset name is the location folder name
            "data.tsv",         //1: assume file name is "data.tsv"
            values.location,    //2: folder
            values.disease(),   //3: disease abbreviation
            values.disease(),   //4: disease full name (not present in the Json file, using abbreviation instead)
            values.platform(),  //5: platform abbreviation
            values.platform(),  //6: platform full name (not present in the Json file, using abbreviation instead)
            values.level(),     //7: level (2 or 3)
            null                //8: scale - since normalization analysis was introduced we no longer scale presets
    };
  }

  @Override
  public Preset createPreset (Object[] values) throws PresetException{
    Preset newPreset = tcgaPresetProvider.get ();
    return newPreset.init(values);
  }

  @PostConstruct
  private void init(){
    if(this.mapper==null)
      this.mapper = new ObjectMapper();
  }

  @NoArgsConstructor()
  @AllArgsConstructor()
  @Accessors(fluent = true)
  private static class TcgaPresetEntry{
    private @JsonProperty @Getter String center;
    private @JsonProperty @Getter String disease;
    private @JsonProperty @Getter String platform;
    private @JsonProperty @Getter String level;
    private @JsonProperty @Getter String location;
  }

  @Override
  @SneakyThrows({IOException.class})
  public List<Preset> getAll(URL data) throws PresetException{

    if(this.mapper==null)
      this.init();

    List<Preset> result = new ArrayList<Preset>();
    for(TcgaPresetEntry entry : mapper.readValue(data, TcgaPresetEntry[].class)){
      result.add(createPreset (formatPreset(entry)));
    }

    return result;
  }
}
