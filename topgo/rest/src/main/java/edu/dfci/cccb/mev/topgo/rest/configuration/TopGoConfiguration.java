package edu.dfci.cccb.mev.topgo.rest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter;
import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.topgo.domain.TopGo;
import edu.dfci.cccb.mev.topgo.domain.TopGoBuilder;

@Configuration
@ComponentScan ("edu.dfci.cccb.mev.topgo.rest.controllers")
public class TopGoConfiguration extends MevRestConfigurerAdapter {

  @Bean
  @Scope ("prototype")
  public TopGoBuilder topgoBuilder () {
    return new TopGoBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<TopGo> topgoAnalysisPathVariableMethodArgumentResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (TopGo.class);
  }
}
