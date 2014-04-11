package edu.dfci.cccb.mev.test.presets.tools;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;

@Configuration
@Import({PresetsRestConfiguration.class})
public class PresetsRestToolConfiguration {

  @Bean @Profile("local") @Inject 
  public String prefetchPresetRowKeys(final Presets presets, 
                                    final PresetDimensionBuilder builder,
                                    final @Named("mev-presets-loader") PresetValuesLoader loader) throws PresetException{
    return "done";
  }
  

  
}

