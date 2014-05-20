package edu.dfci.cccb.mev.presets.rest.configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



@Configuration
@ComponentScan(value="edu.dfci.cccb.mev.presets", 
  includeFilters = @Filter (type = ANNOTATION, value = {Controller.class, RestController.class }),
  excludeFilters=@Filter(type=ANNOTATION, value = {Configuration.class}))
@PropertySources({
  @PropertySource ("classpath:/presets.properties"),
  @PropertySource (value="classpath:/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true)
})
@Import(value={PresetsFilesConfig.class, PresetFlatFileConfig.class})
public class PresetsRestConfiguration extends WebMvcConfigurerAdapter {

  
}
