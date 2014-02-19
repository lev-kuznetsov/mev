package edu.dfci.cccb.mev.test.annotation.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Profile("test")
@Configuration
public class AnnotationServerConfigurationMock extends WebMvcConfigurerAdapter{
  
}
