package edu.dfci.cccb.mev.dataset.rest.configuration;

import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ParserFactory;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelectionBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvComposerFactory;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;

@Configuration
public class DatasetDomainBuildersConfiguration {

  // Domain builders

  @Bean
  @Primary
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public ValueStoreBuilder valueFactory (/* @Named ("mev-datasource") DataSource
                                          * dataSource */) throws Exception {
    // return new SharedCachedValueStoreBuilder (new
    // JooqBasedDatasourceValueStoreBuilder (dataSource));
    // return new MetamodelBackedValueStoreBuilder ();
    return new FlatFileValueStoreBuilder ();
  }

  @Bean
  @Primary
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public DatasetBuilder datasetBuilder () {
    return new SimpleDatasetBuilder ();
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public SelectionBuilder selectionBuilder () {
    return new SimpleSelectionBuilder ();
  }

  @Bean
  public SuperCsvComposerFactory superCsvComposerFactory () {
    return new SuperCsvComposerFactory ();
  }

  @Bean
  public ParserFactory tsvParserFactory () {
    return new SuperCsvParserFactory ();
  }
}
