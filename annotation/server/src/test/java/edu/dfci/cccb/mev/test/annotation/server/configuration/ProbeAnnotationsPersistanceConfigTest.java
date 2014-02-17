package edu.dfci.cccb.mev.test.annotation.server.configuration;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsLoaderConfiguration;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsPersistenceConfiguration;
import static edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsPersistenceConfiguration.*;

@Log4j
@Profile("test")
@Configuration 
@Import(ProbeAnnotationsLoaderConfiguration.class)
public class ProbeAnnotationsPersistanceConfigTest {

  @Inject Environment environment; 
  @Bean(name="probe-annotations-root")
  public URL tcgaPresetRoot() throws IOException{
    ClassPathResource classPathResource = new ClassPathResource ("/array_annotations/");
    log.info ("***tcgaPresetRoot-TEST:"+classPathResource.getURL ()+"****");
    return classPathResource.getURL ();
  }
  
}
