package edu.dfci.cccb.mev.annotation.server.configuration;

import java.io.IOException;
import java.net.URL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Import (AnnotationServerConfiguration.class)
public class AnnotationServerConfigurationMock extends WebMvcConfigurerAdapter{

  @Bean(name="probe-annotations-root")
  public URL tcgaPresetRoot() throws IOException{    
    return (new ClassPathResource ("array_annotations/")).getURL ();
  }

}
