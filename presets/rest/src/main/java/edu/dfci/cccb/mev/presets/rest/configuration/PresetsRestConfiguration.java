package edu.dfci.cccb.mev.presets.rest.configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.configuration.util.archaius.ArchaiusConfig;
import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.configuration.util.simple.SimpleConfig;


@Log4j
@Configuration
@ComponentScan(value="edu.dfci.cccb.mev.presets", 
  includeFilters = @Filter (type = ANNOTATION, value = {Controller.class, RestController.class }),
  excludeFilters=@Filter(type=ANNOTATION, value = {Configuration.class}))
//@PropertySources({
//  @PropertySource ("classpath:/presets.properties"),
//  @PropertySource (value="classpath:/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true),
//  @PropertySource (value="file:${MEV_CONFIG_DIR}/presets.properties",ignoreResourceNotFound=true),
//  @PropertySource (value="file:${MEV_CONFIG_DIR}/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true)
//})
@Import(value={PresetsFilesConfig.class, PresetFlatFileConfig.class})
public class PresetsRestConfiguration extends WebMvcConfigurerAdapter {
  @Inject private Environment environment;
  @Bean(name="presets-config")
  public Config getPresetsConfig() throws IOException, URISyntaxException{
    log.debug (String.format("*****environment: %s", this.environment));
    return new ArchaiusConfig("presets.properties");
  }
  
  @Bean(name="presets-persistence-config") 
  public Config getConfig(){    
    return new ArchaiusConfig ("persistence/presets.persistence.properties");
  }
}
