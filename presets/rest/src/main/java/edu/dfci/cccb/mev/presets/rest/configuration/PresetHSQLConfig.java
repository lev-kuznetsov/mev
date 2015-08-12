package edu.dfci.cccb.mev.presets.rest.configuration;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.dal.HSQLPresetLoader;
import edu.dfci.cccb.mev.presets.dataset.flat.PresetDatasetBuilderFlatTableDB;
import edu.dfci.cccb.mev.presets.dataset.flat.PresetDimensionBuilderFlatTable;
import lombok.extern.log4j.Log4j;

@Log4j
@Configuration
//@PropertySources({
//  @PropertySource ("classpath:/presets.properties"),
//  @PropertySource (value="classpath:/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true)
//})
@Import({PresetPersistenceConfiguration.class})
public class PresetHSQLConfig {
  private final static String TCGA_PROPERTY_DATASET_RELOAD_FLAG="mev.presets.tcga.data.reload";
  @Inject Environment environment;
  
  @Bean(name="mev-presets-loader") @Inject 
  public PresetValuesLoader presestValuesLoader(@Named ("presets-datasource") DataSource dataSource){    
    String reloadFlag = environment.getProperty (TCGA_PROPERTY_DATASET_RELOAD_FLAG);
    log.info (TCGA_PROPERTY_DATASET_RELOAD_FLAG+":" + reloadFlag);
    return new HSQLPresetLoader (dataSource, Boolean.parseBoolean (reloadFlag), 1000);
  }
  
  @Bean(name="presets-dataset-builder") @Inject 
  public PresetDatasetBuilder presetDatasetBuilder(@Named("presets-datasource") DataSource dataSource, 
                                                   @Named("presets-jooq-context") DSLContext context,
                                                   PresetDimensionBuilder dimensionBuilder) throws SQLException{
    log.debug ("***PresetDataSetBuilder: FLATTABLE-DB");
    return new PresetDatasetBuilderFlatTableDB (dataSource, context, presetDimensionBuilder(context));
  }
  
  @Bean @Inject
  public PresetDimensionBuilder presetDimensionBuilder(@Named("presets-jooq-context") DSLContext context){
    log.debug ("***PresetDIMENSIONBuilder: FLATTABLE-DB");
//  PresetDimensionBuilder builder = new PresetDimensionBuilderFlatTable (context);
    PresetDimensionBuilder builder = new PresetDimensionBuilderFlatTable(context);
    return builder;
  }
  
//  @Bean @Profile("!test") @Inject 
//  public String prefetchPresetRowKeys(final Presets presets, 
//                                    final PresetDimensionBuilder builder,
//                                    final @Named("mev-presets-loader") PresetValuesLoader loader) throws PresetException{    
//    loader.loadAll (presets);       
//    (new Thread(new Runnable() {
//      
//      @Override
//      public void run () {
//        for(Preset preset : presets.getAll ()){           
//          log.debug ("***Prefetching row keys for PRESET: "+preset.name ());
//          try {
//            builder.buildRows (preset.descriptor ());
//          } catch (PresetException e) {
//            e.printStackTrace();
//          }
//        }
//      }
//    }
//    )).start();
//    
////    for(final Preset preset : presets.getAll ()){
////      (new Thread(new Runnable() {
////      
////        @Override
////        public void run () {                   
////            log.debug ("***Prefetching row keys for PRESET: "+preset.name ());
////            builder.buildRows (preset.descriptor ());        
////        }
////      }
////      )).start();      
////    }
//    
//    return "done";
//  }
}
