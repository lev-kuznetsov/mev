package edu.dfci.cccb.mev.test.geods.rest.configuration;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import edu.dfci.cccb.mev.geods.rest.configuration.GeoDatasetsConfigurationMain;

@Log4j
@Configuration
@Import({GeoDatasetsConfigurationMain.class})
@Profile("test")
public class GeoDatasetsConfigurationTEST {
  @Inject Environment environment;
  @Bean(name="GeoFtpRootUrl")
  public URL geoFTPRoot() throws MalformedURLException{
    URL url = this.getClass ().getResource ("root/");
    log.debug ("Geo Ftp Root - TEST:"+ url);
    return url;
  }
}
