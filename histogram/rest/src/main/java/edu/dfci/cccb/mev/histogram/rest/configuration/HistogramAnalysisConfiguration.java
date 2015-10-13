package edu.dfci.cccb.mev.histogram.rest.configuration;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramAnalysis;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramAnalysisBuilder;
import edu.dfci.cccb.mev.histogram.domain.impl.SimpleHistogramAnalysisBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.histogram.rest.controllers")
public class HistogramAnalysisConfiguration {

  @Bean(name="histogram.analysis.builder")
  @Scope ("prototype")
  public SimpleHistogramAnalysisBuilder builder () {
    return new SimpleHistogramAnalysisBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<HistogramAnalysis> resolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (HistogramAnalysis.class);
  }
}
