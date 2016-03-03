package edu.dfci.cccb.mev.pe.rest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter;
import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.pe.domain.PathwayEnrichment;
import edu.dfci.cccb.mev.pe.domain.PathwayEnrichmentBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.pe.rest.controllers")
public class PathwayEnrichmentConfiguration extends MevRestConfigurerAdapter {

  @Bean
  @Scope ("prototype")
  public PathwayEnrichmentBuilder pathwayEnrichmentBuilder () {
    return new PathwayEnrichmentBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<PathwayEnrichment> pathwayEnrichmentVariableMethodArgumentResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (PathwayEnrichment.class);
  }
}
