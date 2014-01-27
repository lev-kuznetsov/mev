package edu.dfci.cccb.mev.presets.rest.configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.simple.PresestsSimple;

@Log4j
@Configuration
@ComponentScan(value="edu.dfci.cccb.mev.presets", 
  includeFilters = @Filter (type = ANNOTATION, value = {Controller.class, RestController.class }))
@PropertySource ("classpath:/presets.properties")
public class PresetsRestConfiguration extends WebMvcConfigurerAdapter {

  @Inject Environment environment;
  
  @Bean  
  public Presets getTcgaPresets(URL tcgaPresetRoot) {
    String pathMetadata = environment.getProperty ("mev.presets.tcga.metadata.file");
    log.info ("mev.presets.tcga.metadata.file:" + pathMetadata);
    if(pathMetadata == null)
      return null;
    
    File fileMetadata = new File(pathMetadata);
    if(fileMetadata.exists ()==false)
      return null;
    
    return new PresestsSimple (fileMetadata);
  }
  
  @Bean (name="tcgaPresetRoot")
  public URL tcgaPresetsRoot() throws MalformedURLException{
    String pathTcgaRoot = environment.getProperty ("mev.presets.tcga.metadata.root");
    log.info ("mev.presets.tcga.metadata.root:" + pathTcgaRoot);
    if(pathTcgaRoot == null)
      return null;
    
    URL tcgaPresetRootURL = new URL("file:"+pathTcgaRoot);
    return tcgaPresetRootURL;
  }
}
