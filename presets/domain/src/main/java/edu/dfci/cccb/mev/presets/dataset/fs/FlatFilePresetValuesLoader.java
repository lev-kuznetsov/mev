package edu.dfci.cccb.mev.presets.dataset.fs;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.inject.Inject;
import javax.naming.OperationNotSupportedException;

import org.apache.commons.io.FilenameUtils;
import org.jooq.tools.StringUtils;

import com.google.common.base.Joiner;

import lombok.Setter;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
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
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetValuesLoader;

public class FlatFilePresetValuesLoader extends AbstractPresetValuesLoader implements PresetValuesLoader{

  @Setter @Inject private DatasetBuilder datasetBuilder;
  
  @Override
  public void loadAll (Presets presets) throws PresetException {
    for(Preset preset : presets.getAll ())
      load(preset);
  }

  @Override
  public void load (Preset preset) throws PresetException {
    load(preset.descriptor ());  
  }

  @Override
  public void load (PresetDescriptor descriptor) throws PresetException {
    try {
      RawInput rawInput = new UrlTsvInput (descriptor.dataUrl ());
      PresetDescriptorFlatFileAdaptor flatFileDescriptor = new PresetDescriptorFlatFileAdaptor (descriptor);
      
      Path pathToFile = Paths.get(flatFileDescriptor.binaryUrl ().toURI ());
      Files.createDirectories(pathToFile.getParent());
      
      File valuesFile = new File(flatFileDescriptor.binaryUrl ().toURI ());    
      datasetBuilder.setValueStoreBuilder (new FlatFileValueStoreBuilder (valuesFile));
      Dataset dataset = datasetBuilder.build (rawInput);
      
      
      writeKeys(dataset.dimension (Type.COLUMN).keys (), flatFileDescriptor.columnListUrl ());
      writeKeys(dataset.dimension (Type.ROW).keys (), flatFileDescriptor.rowListUrl ());
      
    } catch (URISyntaxException | IOException | DatasetBuilderException | InvalidDatasetNameException | InvalidDimensionTypeException e) {
      throw new PresetException ("Error loading preset descriptor="+descriptor,  e);
    }
  }
    
  private void writeKeys(List<String> keys, URL url) throws IOException, URISyntaxException{    
    String join = Joiner.on ('\t').join (keys);
    Files.write(Paths.get(url.toURI ()), join.getBytes());
  }
  
  @Override
  public void load (URL url) throws PresetException {    
     throw new PresetException ("FlatFile dataset cannot be loaded from URL, use a Preset object instead; url="+url, new OperationNotSupportedException ());
  }

}
