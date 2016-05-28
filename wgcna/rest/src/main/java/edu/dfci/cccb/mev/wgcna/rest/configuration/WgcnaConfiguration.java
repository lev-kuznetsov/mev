package edu.dfci.cccb.mev.wgcna.rest.configuration;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.wgcna.domain.Wgcna;
import edu.dfci.cccb.mev.wgcna.domain.WgcnaBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by antony on 5/27/16.
 */
@Configuration
@ComponentScan("edu.dfci.cccb.mev.wgcna.rest.controllers")
public class WgcnaConfiguration {
    @Bean(name = "mev.analysis.wgcna.builder")
    @Scope("prototype")
    public WgcnaBuilder wgcnaBuilder () {
        return new WgcnaBuilder ();
    }

    @Bean
    public AnalysisPathVariableMethodArgumentResolver<Wgcna> wgcnaResolver () {
        return new AnalysisPathVariableMethodArgumentResolver<> (Wgcna.class);
    }
}
