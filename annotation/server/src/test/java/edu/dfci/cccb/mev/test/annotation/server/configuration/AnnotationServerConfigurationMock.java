package edu.dfci.cccb.mev.test.annotation.server.configuration;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration;

@Profile("test")
@Configuration
public class AnnotationServerConfigurationMock extends WebMvcConfigurerAdapter{
  
  
  @Inject
  private Environment environment;
  
}
