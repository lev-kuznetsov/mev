package edu.dfci.cccb.mev.survival.rest.configuration;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalAnalysisBuilder;
import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalAnalysisBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.survival.rest.controllers")
public class SurvivalAnalysisConfiguration {

  @Bean
  @Named("survival.analysis.builder")
  @Scope ("prototype")
  public SimpleSurvivalAnalysisBuilder builder(){
    return new SimpleSurvivalAnalysisBuilder();
  }
}
