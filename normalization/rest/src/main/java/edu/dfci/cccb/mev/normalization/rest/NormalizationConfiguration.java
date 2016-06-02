package edu.dfci.cccb.mev.normalization.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.normalization.domain.Normalization;
import edu.dfci.cccb.mev.normalization.domain.NormalizationBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.normalization.rest.controllers")
public class NormalizationConfiguration {

  @Bean
  @Scope ("prototype")
  public NormalizationBuilder normalizationBuilder() {
    return new NormalizationBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<Normalization> normalizationResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (Normalization.class);
  }
}
