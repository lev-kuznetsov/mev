package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.springframework.context.annotation.ScopedProxyMode.NO;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.annotation.elasticsearch.preset.TcgaPresestMetafileColumnCsv;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsFilesConfiguration;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.tcga.TcgaPresetMetafile;
import edu.dfci.cccb.mev.presets.tools.loader.PresetsLoaderConfiguration;

@Configuration
@Import({ProbeAnnotationsFilesConfiguration.class, PresetsLoaderConfiguration.class, ElasticSearchConfiguration.class})
public class PresetsImportAppConfiguration {
  @Bean 
  @Scope(value=SCOPE_PROTOTYPE, proxyMode=NO)
  @Named("tcgaPreset")
  public Preset tcgaPreset(){
    return new TcgaPresestMetafileColumnCsv();
  }
  
}
