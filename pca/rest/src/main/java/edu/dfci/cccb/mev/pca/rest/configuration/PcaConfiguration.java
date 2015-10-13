package edu.dfci.cccb.mev.pca.rest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.pca.domain.Pca;
import edu.dfci.cccb.mev.pca.domain.PcaBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.pca.rest.controllers")
public class PcaConfiguration {

  @Bean
  @Scope ("prototype")
  public PcaBuilder pca () {
    return new PcaBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<Pca> resolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (Pca.class);
  }
}
