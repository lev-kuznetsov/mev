package edu.dfci.cccb.mev.genemad.rest.configuration;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADAnalysis;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADAnalysisBuilder;
import edu.dfci.cccb.mev.genemad.domain.impl.RserveGeneMADAnalysisBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.genemad.rest.controllers")
public class GeneMADAnalysisConfiguration {

  @Bean(name="genemad.analysis.builder")
  @Scope ("prototype")
  public RserveGeneMADAnalysisBuilder builder () {
    return new RserveGeneMADAnalysisBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<GeneMADAnalysis> resolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (GeneMADAnalysis.class);
  }
}
