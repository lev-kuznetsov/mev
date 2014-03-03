package edu.dfci.cccb.mev.test.presets.rest.configuration;


import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetDomainBuildersConfiguration;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;

@Log4j
@Configuration
@Import ({DatasetDomainBuildersConfiguration.class, PresetsRestConfiguration.class})
public class PresetsRestConfigurationTest extends WebMvcConfigurerAdapter{

  @Inject Environment environment;
  
  @Bean (name="tcgaPresetRoot")
  public URL tcgaPresetRoot() throws IOException{    
    return (new ClassPathResource ("tcga/")).getURL ();
  }

  @Inject @Bean
  public DataSourceInitializer dataSourceInitializer( @Named("presets-datasource") DataSource dataSource) {
      DataSourceInitializer initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource);

      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
      
      String scriptDropAll = environment.getRequiredProperty("mev.presets.db.schema.script.dropall");
      log.info ("***dataSourceInitializer-dropAllPresetsScript-TEST:"+scriptDropAll);
      //populator.addScript(new ClassPathResource(scriptDropAll));
      //populator.addScript(new ClassPathResource("testPresetValuesFlatTable.sql"));
            
      initializer.setDatabasePopulator(populator);
      return initializer;
  }
}
