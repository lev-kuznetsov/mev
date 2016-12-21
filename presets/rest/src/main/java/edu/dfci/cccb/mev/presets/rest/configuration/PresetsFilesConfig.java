package edu.dfci.cccb.mev.presets.rest.configuration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import edu.dfci.cccb.mev.presets.tcga.*;
import org.apache.commons.configuration.FileSystem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.io.utils.CCCPHelpers;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.simple.SimplePresests;
import lombok.extern.log4j.Log4j;

@Log4j
@Configuration
//@PropertySources({
//  @PropertySource ("classpath:/presets.properties"),
//  @PropertySource (value="classpath:/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true),
//  @PropertySource (value="file:${MEV_CONFIG_DIR}/presets.properties",ignoreResourceNotFound=true),
//  @PropertySource (value="file:${MEV_CONFIG_DIR}/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true),
//})
public class PresetsFilesConfig {
  private final static String TCGA_PROPERTY_MATA_FILENAME="mev.presets.tcga.metadata.filename";
  private final static String TCGA_PROPERTY_ROOT_FOLDER="mev.presets.tcga.metadata.root";
//  @Inject Environment environment;
  @Inject @Named("presets-config") Config environment;

  private URL getMetadataURL(URL tcgaPresetRoot, String metadataFilename) throws MalformedURLException, PresetException {
    URL metadataURL = new URL(tcgaPresetRoot, metadataFilename, null);
    log.info ("URL.getFile():" + metadataURL.getFile ());
    log.info ("URL.toString():" + metadataURL.toString ());
    log.info ("URL.getProtocol ():" + metadataURL.getProtocol ());
    if(!CCCPHelpers.UrlUtils.checkExists(metadataURL)){
      throw new PresetException ("Metadata resource not found: " + metadataURL.toString ());
    }
    return metadataURL;
  }

  @Bean(name="tcgaPresetBiulder")
  public TcgaPresetsBuilder getTcgaPresetsBuilderTsv(){
    return new TcgaPresetsBuilder(TcgaPresetMetafile.class);
  }

  @Bean(name="tcgaPresetBiulder2")
  public TcgaPresetsBuilderJson getTcgaPresetsBuilder2(){
    return new TcgaPresetsBuilderJson(TcgaPresetMetafile2.class);
  }

  @Bean(name="tcgaPresetBiulder3")
  public TcgaPresetsBuilderJson getTcgaPresetsBuilder3(){
    return new TcgaPresetsBuilderJson(TcgaPresetMetafile3.class);
  }

  @Bean(name="tcgaPresetBiulderFS")
  public TcgaPresetsBuilderFS getTcgaPresetsBuilderFS(){
    return new TcgaPresetsBuilderFS(TcgaPresetMetafileFS.class);
  }


  @Bean
  public Presets getTcgaPresets(@Named("tcgaPresetRoot") URL tcgaPresetRoot,
                                @Named("tcgaPresetBiulder") TcgaPresetsBuilder builder,
                                @Named("tcgaPresetBiulder2")TcgaPresetsBuilderJson builder2,
                                @Named("tcgaPresetBiulder3")TcgaPresetsBuilderJson builder3,
                                @Named("tcgaPresetBiulderFS")TcgaPresetsBuilderFS builderFs)
          throws URISyntaxException, PresetException, IOException {

    log.info (TCGA_PROPERTY_ROOT_FOLDER+" URL:" + tcgaPresetRoot);

    String[] metadataFilenames = environment.getStringArray (TCGA_PROPERTY_MATA_FILENAME);
    log.info (TCGA_PROPERTY_MATA_FILENAME+":" + metadataFilenames);
    if(metadataFilenames.length == 0)
      return new SimplePresests();

    Presets allPresets  = new SimplePresests();
    for(String metadataFilename : metadataFilenames){

      log.info (TCGA_PROPERTY_MATA_FILENAME+":" + metadataFilename);
      URL metadataURL = getMetadataURL(tcgaPresetRoot, metadataFilename);

      if(FilenameUtils.getBaseName(metadataFilename).equals("mev.file_metadata")){
        allPresets.put(new SimplePresests (metadataURL, builder));
      } else if(FilenameUtils.getBaseName(metadataFilename).equals("tcga2")){
        allPresets.put(new SimplePresests (metadataURL, builder2));
      } else if(FilenameUtils.getBaseName(metadataFilename).equals("tcga3")) {
        allPresets.put(new SimplePresests (metadataURL, builder3));
      } else if(metadataFilename.endsWith("/")) {
        allPresets.put(new SimplePresests (metadataURL, builderFs));
      }else
        throw new PresetException ("Invalid preset file extension: " + metadataURL.toString ());
    }
    return allPresets;
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
