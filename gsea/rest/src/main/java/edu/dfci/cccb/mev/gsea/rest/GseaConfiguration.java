package edu.dfci.cccb.mev.gsea.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.gsea.domain.Gsea;
import edu.dfci.cccb.mev.gsea.domain.GseaBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.gsea.rest.controllers")
public class GseaConfiguration {

  @Bean
  @Scope ("prototype")
  public GseaBuilder gseaBuilder () {
    return new GseaBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<Gsea> gseaAnalysisResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (Gsea.class);
  }
}
