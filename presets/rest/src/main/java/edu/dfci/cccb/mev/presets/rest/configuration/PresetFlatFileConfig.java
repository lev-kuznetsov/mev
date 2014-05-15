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
import edu.dfci.cccb.mev.presets.dataset.fs.FlatFilePresetValuesLoader;
import edu.dfci.cccb.mev.presets.dataset.fs.PresetDatasetBuilderFlatFile;
import edu.dfci.cccb.mev.presets.dataset.fs.PresetDimensionBuilderFlatFile;
import lombok.extern.log4j.Log4j;

@Log4j
@Configuration
@PropertySources({
  @PropertySource ("classpath:/presets.properties"),
  @PropertySource (value="classpath:/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true)
})
@Import ({PresetHSQLConfigX.class})
public class PresetFlatFileConfig {
  private final static String TCGA_PROPERTY_DATASET_RELOAD_FLAG="mev.presets.tcga.data.reload";
  @Inject Environment environment;
  
  @Bean(name="mev-presets-loader") @Inject 
  public PresetValuesLoader presestValuesLoader(){
    return new FlatFilePresetValuesLoader();
  }
  
  @Bean(name="presets-dataset-builder") @Inject 
  public PresetDatasetBuilder presetDatasetBuilder(PresetDimensionBuilder dimensionBuilder) throws SQLException{
    log.debug ("***PresetDataSetBuilder: FLAT-FILE");
    return new PresetDatasetBuilderFlatFile (dimensionBuilder);
  }
  
  @Bean @Inject
  public PresetDimensionBuilder presetDimensionBuilder(){
    log.debug ("***PresetDIMENSIONBuilder: FLAT-FILE");
    return new PresetDimensionBuilderFlatFile();
  }
}
