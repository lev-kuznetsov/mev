package edu.dfci.cccb.mev.geods.rest.configuration;

import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import edu.dfci.cccb.mev.goeds.domain.contract.GeoDatasetBuilder;
import edu.dfci.cccb.mev.goeds.domain.dataset.ftp.GeoDatasetBuilderFTP;


@Configuration
public class GeoDatasetsBuilderConfig {
  
  @Bean(name="GeoDatasetBuilder")
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public GeoDatasetBuilder getGeodsDatasetBuilder(){
    return new GeoDatasetBuilderFTP ();
  }
  
}
