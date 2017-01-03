package edu.dfci.cccb.mev.test.presets.rest.configuration;


import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import edu.dfci.cccb.mev.presets.rest.configuration.PresetHSQLConfig;
import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetDomainBuildersConfiguration;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;

@Log4j
@Profile("test")
@Configuration
@Import ({DatasetDomainBuildersConfiguration.class, PresetsRestConfiguration.class, ProbeAnnotationsPersistanceConfigTest.class, PresetHSQLConfig.class})
public class PresetsRestConfigurationTest extends WebMvcConfigurerAdapter{

//  @Inject Environment environment;
  @Inject @Named("presets-persistence-config") private Config environment;
  
  @Bean (name="tcgaPresetRoot")
  public URL tcgaPresetRoot() throws IOException{    
    log.info ("**** TEST Prests Root Config ****");
    return (new ClassPathResource ("tcga/")).getURL ();
  }

  @Inject @Bean
  public DataSourceInitializer dataSourceInitializer( @Named("presets-datasource") DataSource dataSource) {
      DataSourceInitializer initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource);

      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
      
      String scriptDropAll = environment.getProperty("mev.presets.db.schema.script.dropall");
      log.info ("***dataSourceInitializer-dropAllPresetsScript-TEST:"+scriptDropAll);
      //populator.addScript(new ClassPathResource(scriptDropAll));
      String scriptTestPresetValuesFlatTable = environment.getProperty("mev.presets.db.schema.script.TestPresetValuesFlatTable");
      populator.addScript(new ClassPathResource(scriptTestPresetValuesFlatTable));
            
      initializer.setDatabasePopulator(populator);
       return initializer;
  }
}
