package edu.dfci.cccb.mev.presets.rest.configuration;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.springframework.context.annotation.ScopedProxyMode.NO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.configuration.util.simple.SimpleConfig;
import edu.dfci.cccb.mev.io.utils.CCCPHelpers;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.simple.SimplePresests;
import edu.dfci.cccb.mev.presets.tcga.TcgaPresetMetafile;
import edu.dfci.cccb.mev.presets.tcga.TcgaPresetsBuilder;
import lombok.extern.log4j.Log4j;

@Log4j
@Configuration
@PropertySources({
  @PropertySource ("classpath:/presets.properties"),
  @PropertySource (value="classpath:/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true),
  @PropertySource (value="file:${MEV_CONFIG_DIR}/presets.properties",ignoreResourceNotFound=true),
  @PropertySource (value="file:${MEV_CONFIG_DIR}/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true),
})
public class PresetsFilesConfig {
  private final static String TCGA_PROPERTY_MATA_FILENAME="mev.presets.tcga.metadata.filename";
  private final static String TCGA_PROPERTY_ROOT_FOLDER="mev.presets.tcga.metadata.root";
  @Inject Environment environment;
  
  @Bean  @Inject
  public Presets getTcgaPresets(@Named("tcgaPresetRoot") URL tcgaPresetRoot,
                                @Named("presets-config") Config config,
                                TcgaPresetsBuilder builder                                
                                ) throws URISyntaxException, PresetException, IOException {
    
    String metadataFilename = environment.getProperty (TCGA_PROPERTY_MATA_FILENAME);    
    log.info (TCGA_PROPERTY_ROOT_FOLDER+" URL:" + tcgaPresetRoot);
    log.info (TCGA_PROPERTY_MATA_FILENAME+":" + metadataFilename);
    
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
  
  @Inject
  @Bean (name="tcgaPresetRoot")   
  public URL tcgaPresetRoot(@Named("presets-config") Config conf) throws IOException{    
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
}
