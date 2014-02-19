package edu.dfci.cccb.mev.test.annotation.server.configuration;

import static edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsConfigurationMain.MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsLoaderConfiguration;

@Log4j
@Profile("test")
@Configuration 
@Import(ProbeAnnotationsLoaderConfiguration.class)
public class ProbeAnnotationsPersistanceConfigTest {

  @Inject Environment environment; 
  
  
  @Bean(name="probe-annotations-root")
  public URL probeAnnotationsRoot() throws IOException{
    ClassPathResource classPathResource = new ClassPathResource ("/array_annotations/");
    log.info ("***probeAnnotationsRoot-TEST:"+classPathResource.getURL ()+"****");
    return classPathResource.getURL ();
  }
  @Bean(name="probe-annotations-metafile")
  public URL probeAnnotationsMetafile() throws IOException{
    ClassPathResource classPathResource = new ClassPathResource ("/array_annotations/ProbeAnnotationSources_metafile.tsv");
    log.info ("***probeAnnotationsMetgafile-TEST:"+classPathResource.getURL ()+"****");
    return classPathResource.getURL ();
  }
  
 
  @Inject @Bean
  public DataSourceInitializer dataSourceInitializer( @Named("probe-annotations-datasource") DataSource dataSource) {
      DataSourceInitializer initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource);

      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
      
      String scriptDropAll = environment.getRequiredProperty(MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"db.schema.script.dropall");
      log.info ("***dataSourceInitializer-dropAllScript-TEST:"+scriptDropAll+"****");
      populator.addScript(new ClassPathResource(scriptDropAll));
      
      String scriptCreate = environment.getRequiredProperty(MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"db.schema.script");
      log.info ("***dataSourceInitializer-createScript-TEST:"+scriptCreate+"****");
      populator.addScript(new ClassPathResource(scriptCreate));
      
      
      initializer.setDatabasePopulator(populator);
      return initializer;
  }
  
  
}
