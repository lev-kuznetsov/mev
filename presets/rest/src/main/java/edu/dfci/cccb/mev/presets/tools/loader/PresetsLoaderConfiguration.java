package edu.dfci.cccb.mev.presets.tools.loader;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.dal.HSQLPresetLoader;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;

@Configuration
@ComponentScan(value="edu.dfci.cccb.mev.presets", includeFilters = @Filter (type = ANNOTATION, value = {Component.class }))
@PropertySources({
@PropertySource ("classpath:/presets.properties"),
@PropertySource (value="classpath:/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true)
})
@Import(value={PresetsRestConfiguration.class})
public class PresetsLoaderConfiguration {
  
    
  @Bean(name="mev-presets-loader") @Inject 
  public PresetValuesLoader presestValuesLoader(@Named ("presets-datasource") DataSource dataSource){
    return new HSQLPresetLoader (dataSource, false, 1000);
  }
}
