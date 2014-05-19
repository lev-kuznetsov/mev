package edu.dfci.cccb.mev.web.test.presets.controller.flat.stress;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;

@Log4j
@Configuration
@Import(PresetsRestConfiguration.class)
public class PresetRestPerfConfig {

//  @Inject Environment environment;
//  @Bean(name="presets-datasource", destroyMethod = "close")
//  public DataSource dataSource () {
//    BasicDataSource dataSource = new BasicDataSource ();
//    dataSource.setDriverClassName ("org.h2.Driver");
//    dataSource.setUrl ("jdbc:h2:file:"
//                                                        + environment.getProperty ("user.home") 
//                                                        + "/mev/data/tcga/"
//                                                        + "mev-presets-FLAT-GBM.AgilentG4502A_07_2.Level_2.tsv"
//                                                        + ";AUTO_SERVER=TRUE"
//                                                        + ";QUERY_CACHE_SIZE=100000"
//                                                        + ";CACHE_SIZE=1048576");
//    dataSource.setUsername ("sa");
//    dataSource.setPassword ("");
//    
//    log.info ("*** presets-datasource config: " + dataSource.getUrl ());    
//
//    return dataSource;
//  }
  
  @Inject @Bean
  public DataSourceInitializer dataSourceInitializer( @Named("presets-datasource") DataSource dataSource) {
      DataSourceInitializer initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource);
      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();            
      initializer.setDatabasePopulator(populator);
      return initializer;
  }
  
  @Bean @Profile("local") @Inject 
  public String prefetchPresetRowKeys(final Presets presets, 
                                    final PresetDimensionBuilder builder) throws PresetException, InterruptedException{
    
    List<Thread> threads = new ArrayList<Thread>();
    
    for(final Preset preset : presets.getAll ()){
      Thread t = new Thread(new Runnable() {
        @Override
        public void run () {                   
            log.debug ("***Prefetching row keys for PRESET: "+preset.name ());
            try {
              builder.buildRows (preset.descriptor ());
            } catch (PresetException e) {
              e.printStackTrace();
            }        
        }
      });
      t.start();
      threads.add(t);
    }
    for(int i=0;i<threads.size ();i++){
      threads.get(i).join();
    }
    
    return "done";
  }
  
}
