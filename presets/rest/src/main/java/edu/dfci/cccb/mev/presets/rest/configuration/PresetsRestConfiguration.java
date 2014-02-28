package edu.dfci.cccb.mev.presets.rest.configuration;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.springframework.context.annotation.FilterType.ANNOTATION;
import static org.springframework.context.annotation.ScopedProxyMode.NO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.io.utils.CCCPHelpers;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilderByJooq;
import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.contract.PresetValuesStoreBuilderFactory;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.simple.SimplePresests;
import edu.dfci.cccb.mev.presets.tcga.TcgaPresetMetafile;
import edu.dfci.cccb.mev.presets.tcga.TcgaPresetsBuilder;



@Log4j
@Configuration
@ComponentScan(value="edu.dfci.cccb.mev.presets", 
  includeFilters = @Filter (type = ANNOTATION, value = {Controller.class, RestController.class }))
@PropertySources({
  @PropertySource ("classpath:/presets.properties"),
  @PropertySource (value="classpath:/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true)
})
@Import(value={PresetPersistenceConfiguration.class})
public class PresetsRestConfiguration extends WebMvcConfigurerAdapter {

  private final static String TCGA_PROPERTY_MATA_FILENAME="mev.presets.tcga.metadata.filename";
  private final static String TCGA_PROPERTY_ROOT_FOLDER="mev.presets.tcga.metadata.root";
  private final static String TCGA_PROPERTY_DATASET_RELOAD_FLAG="mev.presets.tcga.data.reload";
  
  @Inject Environment environment;
  
  @Bean  @Inject
  public Presets getTcgaPresets(@Named("tcgaPresetRoot") URL tcgaPresetRoot, 
                                TcgaPresetsBuilder builder,
                                PresetValuesLoader loader
                                ) throws URISyntaxException, PresetException, IOException {
    
    String metadataFilename = environment.getProperty (TCGA_PROPERTY_MATA_FILENAME);    
    String reloadFlag = environment.getProperty (TCGA_PROPERTY_DATASET_RELOAD_FLAG);
    
    log.info (TCGA_PROPERTY_ROOT_FOLDER+" URL:" + tcgaPresetRoot);
    log.info (TCGA_PROPERTY_MATA_FILENAME+":" + metadataFilename);
    log.info (TCGA_PROPERTY_DATASET_RELOAD_FLAG+":" + reloadFlag);
    
    if(metadataFilename == null)
      return new SimplePresests();
    
    
    URL metadataURL = new URL(tcgaPresetRoot, metadataFilename, null);
    log.info ("URL.getFile():" + metadataURL.getFile ());
    log.info ("URL.toString():" + metadataURL.toString ());
    log.info ("URL.getProtocol ():" + metadataURL.getProtocol ());
    
    if(!CCCPHelpers.UrlUtils.checkExists(metadataURL)){
      throw new PresetException ("Metadata resource not found: " + metadataURL.toString ());
    }
    
    Presets presets = new SimplePresests (metadataURL, builder);
    if(Boolean.parseBoolean (reloadFlag))
      loader.loadAll (presets);
    return presets;
  }
  
  @Bean 
  @Scope(value=SCOPE_PROTOTYPE, proxyMode=NO)
  @Named("tcgaPreset")
  public Preset tcgaPreset(){
    return new TcgaPresetMetafile ();
  }
  @Bean
  public TcgaPresetsBuilder getTcgaPresetsBuilder(){
    return new TcgaPresetsBuilder ();
  }
  
  @Bean (name="tcgaPresetRoot")
  public URL tcgaPresetRoot() throws IOException{    
    String pathTcgaRoot = environment.getProperty (TCGA_PROPERTY_ROOT_FOLDER);
    log.info ("**** Prests Root Config ****");
    log.info (TCGA_PROPERTY_ROOT_FOLDER+":" + pathTcgaRoot);
    if(pathTcgaRoot == null)
      return null;
    
    if(pathTcgaRoot.endsWith ("/")==false)
      pathTcgaRoot+="/";
    
    //URL tcgaPresetRootURL = new URL(pathTcgaRoot);
    URL tcgaPresetRootURL = ResourceUtils.getURL (pathTcgaRoot);
    
    if(!CCCPHelpers.UrlUtils.checkExists(tcgaPresetRootURL))
      throw new IOException ("TCGA Preset URL resource cannot be openned: "+tcgaPresetRootURL.toString ());

    return tcgaPresetRootURL;
    
  }
  
  @Bean @Inject
  public PresetDatasetBuilder presetDatasetBuilder(PresetValuesStoreBuilderFactory valueStoreInjector){
    return new PresetDatasetBuilderByJooq (valueStoreInjector);
  }
}
