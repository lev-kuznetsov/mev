package edu.dfci.cccb.mev.test.presets.rest.configuration;

import java.io.IOException;
import java.net.URL;

import static org.springframework.context.annotation.ScopedProxyMode.NO;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;

@Configuration
@Import (PresetsRestConfiguration.class)
public class PresetsRestConfigurationMock extends WebMvcConfigurerAdapter{

  @Bean (name="tcgaPresetRoot")
  public URL tcgaPresetRoot() throws IOException{    
    return (new ClassPathResource ("tcga/")).getURL ();
  }

}
