package edu.dfci.cccb.mev.presets.tools.loader;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ParserFactory;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelectionBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvComposerFactory;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;

@Configuration
@ComponentScan(
        value="edu.dfci.cccb.mev.presets",
        includeFilters = @Filter (type = ANNOTATION, value = {Component.class }),
        excludeFilters=@Filter(type=ANNOTATION, value = {Configuration.class })
)
//@PropertySources({
//@PropertySource ("classpath:/presets.properties"),
//@PropertySource (value="classpath:/presets-${spring_profiles_active}.properties",ignoreResourceNotFound=true)
//})
@Import(value={PresetsRestConfiguration.class})
public class PresetsLoaderConfiguration {
  
  
  @Bean
  @Primary
  public ValueStoreBuilder valueFactory (/* @Named ("mev-datasource") DataSource
                                          * dataSource */) throws Exception {
    // return new SharedCachedValueStoreBuilder (new
    // JooqBasedDatasourceValueStoreBuilder (dataSource));
    // return new MetamodelBackedValueStoreBuilder ();
    return new FlatFileValueStoreBuilder ();
  }

  @Bean
  @Primary
  public DatasetBuilder datasetBuilder () {
    return new SimpleDatasetBuilder ();
  }

  @Bean
  public SelectionBuilder selectionBuilder () {
    return new SimpleSelectionBuilder ();
  }
  
  @Bean
  public SuperCsvComposerFactory superCsvComposerFactory () {
    return new SuperCsvComposerFactory ();
  }

  @Bean
  public ParserFactory tsvParserFactory () {
    SuperCsvParserFactory factory = new SuperCsvParserFactory(new SuperCsvParser.RowIdParser() {
      @Override
      public String parse(String value) {
        int index = value.indexOf("|");
        return index >= 0
          ? value.substring(0, index)
          : value;
      }
    });
    factory.addCommentRegExpression("[\\?].+");
    return factory;
  }
  
  
}
