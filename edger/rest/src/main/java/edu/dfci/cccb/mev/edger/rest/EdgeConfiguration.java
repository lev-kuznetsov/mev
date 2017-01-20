package edu.dfci.cccb.mev.edger.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.edger.domain.Edge;
import edu.dfci.cccb.mev.edger.domain.EdgeBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.edger.rest.controllers")
public class EdgeConfiguration {

  @Bean
  @Scope ("prototype")
  EdgeBuilder edgeBuilder () {
    return new EdgeBuilder ();
  }

  @Bean
  AnalysisPathVariableMethodArgumentResolver<Edge> edgeResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (Edge.class);
  }
}
