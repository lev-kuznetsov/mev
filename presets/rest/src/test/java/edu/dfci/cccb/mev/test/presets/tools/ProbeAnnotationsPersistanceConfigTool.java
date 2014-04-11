package edu.dfci.cccb.mev.test.presets.tools;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationPlatforms;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsConfigurationMain;

@Log4j
@Profile("local")
@Configuration 
@Import(ProbeAnnotationsConfigurationMain.class)
public class ProbeAnnotationsPersistanceConfigTool {

  @Inject Environment environment; 
  
  
  @Bean @Inject
  public ProbeAnnotationPlatforms probeAnnotationPlatforms(@Named("probe-annotations-platforms-loader")ProbeAnnotationsLoader loader) throws MalformedURLException, IOException, AnnotationException{
    
    return null;
  }
  
  
  
  
}
