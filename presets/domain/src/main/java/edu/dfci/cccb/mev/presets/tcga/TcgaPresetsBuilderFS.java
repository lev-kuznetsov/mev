package edu.dfci.cccb.mev.presets.tcga;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetsBuilder;
import edu.dfci.cccb.mev.presets.prototype.AbstractTcgaPreset;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public class TcgaPresetsBuilderFS extends AbstractPresetsBuilder {

  public TcgaPresetsBuilderFS(Class<? extends AbstractTcgaPreset> tcgaPresetClass) {
    super(tcgaPresetClass);
  }



  @Override
  @SneakyThrows({IOException.class, URISyntaxException.class})
  public List<Preset> getAll(URL data) throws PresetException{
    List<Preset> result = new ArrayList<Preset>();
    Path dataPath = Paths.get(data.toURI());

    try (DirectoryStream<Path> topLevelPaths = Files.newDirectoryStream(dataPath, new DirectoryStream.Filter<Path>(){
      @Override
      public boolean accept(Path path) throws IOException {
        return Files.isDirectory(path);
      }
    })) {
      for (Path topLevelPath : topLevelPaths) {
        try(DirectoryStream<Path> files = Files.newDirectoryStream(topLevelPath)){
          for(Path file : files){
            TcgaPresetEntry entry = tcgaPresetEntryClass().getConstructor(Path.class)
                    .newInstance(file);
            if(entry.isValid())
              result.add(createPreset(entry.formatPreset()));
          }
        }
      }
    }catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
      throw new PresetException(e);
    }
    return result;
  }
}
