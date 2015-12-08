package edu.dfci.cccb.mev.survival.rest.configuration;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalAnalysis;
import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalAnalysisBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.survival.rest.controllers")
public class SurvivalAnalysisConfiguration {

  @Bean(name="survival.analysis.builder")
  @Scope ("prototype")
  public SimpleSurvivalAnalysisBuilder builder () {
    return new SimpleSurvivalAnalysisBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<SurvivalAnalysis> resolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (SurvivalAnalysis.class);
  }
}
