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
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.hcl.domain.concrete.AverageAlgorithm;
import edu.dfci.cccb.mev.hcl.domain.concrete.EuclideanMetric;
import edu.dfci.cccb.mev.hcl.domain.concrete.TwoDimensionalHcl;
import edu.dfci.cccb.mev.hcl.domain.contract.HclResult;
import edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder;
import edu.dfci.cccb.mev.hcl.domain.mock.MockNodeBuilder;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.BranchJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.HclResultJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.HierarchicallyClusteredDimensionJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.LeafJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.newick.HclResultNewickMessageConverter;
import edu.dfci.cccb.mev.hcl.rest.assembly.newick.NodeNewickMessageConverter;
import edu.dfci.cccb.mev.hcl.rest.context.RestPathVariableHclRequestContextInjector;
import edu.dfci.cccb.mev.hcl.rest.resolvers.AlgorithmPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.hcl.rest.resolvers.MetricPathVariableMethodArgumentResolver;

/**
 * @author levk
 * 
 */
@Configuration
@ToString
@ComponentScan (basePackages = "edu.dfci.cccb.mev.hcl.rest.controllers")
@Import (RestPathVariableHclRequestContextInjector.class)
public class HclRestConfiguration extends WebMvcConfigurerAdapter {

  @Bean
  public NodeBuilder nodeBuilder () {
    return new MockNodeBuilder ();
  }

  @Bean
  @Scope (value = SCOPE_PROTOTYPE, proxyMode = TARGET_CLASS)
  public TwoDimensionalHcl hcl () {
    return new TwoDimensionalHcl ();
  }

  @Bean
  public NodeNewickMessageConverter nodeNewickMessageConverter () {
    return new NodeNewickMessageConverter ();
  }

  @Bean
  HclResultNewickMessageConverter hclResultNewickMessageConverter () {
    return new HclResultNewickMessageConverter ();
  }

  @Bean
  public HclResultJsonSerializer hclResultJsonSerializer () {
    return new HclResultJsonSerializer ();
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
  public HierarchicallyClusteredDimensionJsonSerializer hierarchicallyClusteredDimensionJsonSerializer () {
    return new HierarchicallyClusteredDimensionJsonSerializer ();
  }

  @Bean
  public EuclideanMetric euclideanMetric () {
    return new EuclideanMetric ();
  }

  @Bean
  public AverageAlgorithm averageAlgorithm () {
    return new AverageAlgorithm ();
  }

  @Bean
  public AlgorithmPathVariableMethodArgumentResolver algorithmPathVariableMethodArgumentResolver () {
    return new AlgorithmPathVariableMethodArgumentResolver ();
  }

  @Bean
  public MetricPathVariableMethodArgumentResolver metricPathVariableMethodArgumentResolver () {
    return new MetricPathVariableMethodArgumentResolver ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<HclResult> hclAnalysisPathVariableMethodArgumentResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<HclResult> (HclResult.class);
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
