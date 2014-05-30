package edu.dfci.cccb.mev.geods.rest.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages={"edu.dfci.cccb.mev.geods.rest.controllers"})
@Import({GeoDatasetsBuilderConfig.class, GeoFileConfig.class})
public class GeoDatasetsConfigurationMain {
  
}
