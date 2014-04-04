package edu.dfci.cccb.mev.anova.rest.configuration;

import static java.util.Arrays.asList;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;

import com.fasterxml.jackson.databind.JsonSerializer;

import edu.dfci.cccb.mev.anova.domain.contract.Anova;
import edu.dfci.cccb.mev.anova.domain.contract.AnovaBuilder;
import edu.dfci.cccb.mev.anova.domain.impl.FileBackedAnovaBuilder;
import edu.dfci.cccb.mev.anova.rest.assembly.json.AnovaJsonSerializer;
import edu.dfci.cccb.mev.anova.rest.assembly.json.EntryJsonSerializer;
import edu.dfci.cccb.mev.anova.rest.assembly.tsv.AnovaTsvMessageConverter;
import edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter;
import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;


@Configuration
@ComponentScan ("edu.dfci.cccb.mev.anova.rest.controllers")
public class AnovaRestConfiguration extends MevRestConfigurerAdapter {
  @Bean
  @Scope (SCOPE_REQUEST)
  public AnovaBuilder anovaBuilder () {
    return new FileBackedAnovaBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<Anova> anovaPathVariableMethodArgumentResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (Anova.class);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addJsonSerializers(java.util.List) */
  @Override
  public void addJsonSerializers (List<JsonSerializer<?>> serializers) {
    serializers.addAll (asList (new EntryJsonSerializer (), new AnovaJsonSerializer ()));
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addHttpMessageConverters(java.util.List) */
  @Override
  public void addHttpMessageConverters (List<HttpMessageConverter<?>> converters) {
    converters.add (new AnovaTsvMessageConverter ());
  }
}
