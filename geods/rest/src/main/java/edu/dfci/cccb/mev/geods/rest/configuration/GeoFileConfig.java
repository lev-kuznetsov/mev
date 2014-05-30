package edu.dfci.cccb.mev.geods.rest.configuration;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@Log4j
@Configuration
@PropertySources({
  @PropertySource("classpath:edu/dfci/cccb/mev/geods/rest/configuration/geods.properties"),
  @PropertySource (value="classpath:edu/dfci/cccb/mev/geods/rest/configuration/geods-${spring_profiles_active}.properties",ignoreResourceNotFound=true)
})
public class GeoFileConfig {
  
  @Inject Environment environment;
  @Bean(name="GeoFtpRootUrl")
  public URL geoFTPRoot() throws MalformedURLException{
    String url = environment.getProperty ("ncbi_geo_ftp_root");    
    if(!url.endsWith ("/")) url+="/";    
    log.debug ("Geo Ftp Root - TEST:"+ url);
    return new URL(url);
  }
}
