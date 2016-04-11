package edu.dfci.cccb.mev.test.annotation.server.configuration;

import static edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsFilesConfiguration.MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsConfigurationMain;
import edu.dfci.cccb.mev.configuration.util.contract.Config;

@Log4j
@Profile("test")
@Configuration 
@Import(ProbeAnnotationsConfigurationMain.class)
public class ProbeAnnotationsPersistanceConfigTest {

//  @Inject Environment environment; 
  @Inject @Named("probe-annotations-loader-config") private Config environment;   
  
  @Bean(name="probe-annotations-root")
  public URL probeAnnotationsRoot() throws IOException{
    URL url = this.getClass ().getResource ("array_annotations/");
    log.info ("***probeAnnotationsRoot-TEST:"+url+"****");
    return url;
  }
  @Bean(name="probe-annotatinos-platforms-metafile")
  public URL probeAnnotationsMetafile() throws IOException{
    URL url = this.getClass ().getResource("array_annotations/probe_annotation_platforms_metafile-TEST.tsv");
    log.info ("***probeAnnotationsMetgafile-TEST:"+url);
    return url;
  }  
 
  @Inject @Bean
  public DataSourceInitializer dataSourceInitializer( @Named("probe-annotations-datasource") DataSource dataSource) {
      DataSourceInitializer initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource);

      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
      
      String scriptDropAll = environment.getProperty(MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"db.schema.script.dropall");
      log.info ("***dataSourceInitializer-dropAllScript-TEST:"+scriptDropAll);
      populator.addScript(new ClassPathResource(scriptDropAll));
      
      String scriptCreate = environment.getProperty(MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"db.schema.script");
      log.info ("***dataSourceInitializer-createScript-TEST:"+scriptCreate);
      populator.addScript(new ClassPathResource(scriptCreate));
      
      
      initializer.setDatabasePopulator(populator);
      return initializer;
  }
  
  
}
