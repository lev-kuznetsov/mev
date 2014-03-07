package edu.dfci.cccb.mev.web.test.presets.controller.nvp;

import static java.lang.System.getProperty;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetValuesStoreBuilderFactory;
import edu.dfci.cccb.mev.presets.dataset.PresetDatasetBuilderByJooq;
import edu.dfci.cccb.mev.presets.dataset.flat.PresetDatasetBuilderFlatTable;
import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;

@Log4j
@Configuration
@Import(PresetsRestConfigurationTest.class)
public class TestPresetsDatasetNVPConfig {

  @Inject Environment environment;
  @Bean(name="presets-datasource", destroyMethod = "close")
  public DataSource dataSource () {
    BasicDataSource dataSource = new BasicDataSource ();
    dataSource.setDriverClassName ("org.h2.Driver");
    dataSource.setUrl ("jdbc:h2:file:"
                                                        + environment.getProperty ("user.home") 
                                                        + "/mev/data/tcga/"
                                                        + "mev-presets-NVP"
                                                        + ";QUERY_CACHE_SIZE=100000"
                                                        + ";CACHE_SIZE=1048576");
    dataSource.setUsername ("sa");
    dataSource.setPassword ("");
    
    log.info ("*** presets-datasource config: " + dataSource.getUrl ());    

    return dataSource;
  }

  @Inject @Bean
  public DataSourceInitializer dataSourceInitializer( @Named("presets-datasource") DataSource dataSource) {
      DataSourceInitializer initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource);

      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            
      initializer.setDatabasePopulator(populator);
      return initializer;
  }
  
  @Bean @Inject
  public PresetDatasetBuilder presetDatasetBuilder(PresetValuesStoreBuilderFactory valueStoreInjector) throws SQLException{
    log.debug ("***PresetDataSetBuilder: NVP");
    return new PresetDatasetBuilderByJooq (valueStoreInjector);
  }
}
