/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.hcl.rest.configuration;

import static edu.dfci.cccb.mev.hcl.rest.assembly.newick.NodeNewickMessageConverter.NEWICK_EXTENSION;
import static edu.dfci.cccb.mev.hcl.rest.assembly.newick.NodeNewickMessageConverter.NEWICK_MEDIA_TYPE;
import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;
import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.hcl.domain.contract.Hcl;
import edu.dfci.cccb.mev.hcl.domain.contract.HclBuilder;
import edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder;
import edu.dfci.cccb.mev.hcl.domain.mock.MockNodeBuilder;
import edu.dfci.cccb.mev.hcl.domain.simple.SimpleTwoDimensionalHclBuilder;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.BranchJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.HclJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.LeafJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.SimpleHierarchicallyClusteredDimensionJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.newick.HclNewickMessageConverter;
import edu.dfci.cccb.mev.hcl.rest.assembly.newick.NodeNewickMessageConverter;
import edu.dfci.cccb.mev.hcl.rest.resolvers.LinkagePathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.hcl.rest.resolvers.MetricPathVariableMethodArgumentResolver;
//import org.springframework.context.annotation.Import;
//import edu.dfci.cccb.mev.hcl.rest.context.RestPathVariableHclRequestContextInjector;

/**
 * @author levk
 * 
 */
@Configuration
@ToString
@ComponentScan (basePackages = "edu.dfci.cccb.mev.hcl.rest.controllers")
// @Import (RestPathVariableHclRequestContextInjector.class)
public class HclRestConfiguration extends WebMvcConfigurerAdapter {

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<Hcl> hclPathVariableMethodArgumentResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<Hcl> (Hcl.class);
  }

  @Bean
  public NodeBuilder nodeBuilder () {
    return new MockNodeBuilder ();
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public HclBuilder hclBuilder () {
    return new SimpleTwoDimensionalHclBuilder ();
  }

  @Bean
  public NodeNewickMessageConverter nodeNewickMessageConverter () {
    return new NodeNewickMessageConverter ();
  }

  @Bean
  public HclNewickMessageConverter hclNewickMessageConverter () {
    return new HclNewickMessageConverter ();
  }

  @Bean
  public HclJsonSerializer hclJsonSerializer () {
    return new HclJsonSerializer ();
  }

  @Bean
  public LeafJsonSerializer leafJsonSerializer () {
    return new LeafJsonSerializer ();
  }

  @Bean
  public BranchJsonSerializer branchJsonSerializer () {
    return new BranchJsonSerializer ();
  }

  @Bean
  public SimpleHierarchicallyClusteredDimensionJsonSerializer simpleHierarchicallyClusteredDimensionJsonSerializer () {
    return new SimpleHierarchicallyClusteredDimensionJsonSerializer ();
  }

  @Bean
  public LinkagePathVariableMethodArgumentResolver algorithmPathVariableMethodArgumentResolver () {
    return new LinkagePathVariableMethodArgumentResolver ();
  }

  @Bean
  public MetricPathVariableMethodArgumentResolver metricPathVariableMethodArgumentResolver () {
    return new MetricPathVariableMethodArgumentResolver ();
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #configureContentNegotiation
   * (org.springframework.web.servlet.config.annotation
   * .ContentNegotiationConfigurer) */
  @Override
  public void configureContentNegotiation (ContentNegotiationConfigurer configurer) {
    configurer.mediaType (NEWICK_EXTENSION, NEWICK_MEDIA_TYPE);
  }
}
