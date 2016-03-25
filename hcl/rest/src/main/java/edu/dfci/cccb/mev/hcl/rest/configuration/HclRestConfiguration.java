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
import static java.util.Arrays.asList;
import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.List;

import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Rserve;
import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.hcl.domain.contract.Hcl;
import edu.dfci.cccb.mev.hcl.domain.contract.HclBuilder;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;
import edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder;
import edu.dfci.cccb.mev.hcl.domain.mock.MockNodeBuilder;
import edu.dfci.cccb.mev.hcl.domain.r.RHclBuilder;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.BranchJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.HclJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.HclResultJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.LeafJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.NodeJsonDeserializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.json.SimpleHierarchicallyClusteredDimensionJsonSerializer;
import edu.dfci.cccb.mev.hcl.rest.assembly.newick.HclNewickMessageConverter;
import edu.dfci.cccb.mev.hcl.rest.assembly.newick.NodeNewickMessageConverter;

/**
 * @author levk
 * 
 */
@Configuration
@ToString
@ComponentScan (basePackages = "edu.dfci.cccb.mev.hcl.rest.controllers")
public class HclRestConfiguration extends MevRestConfigurerAdapter {

  @Bean
  public NodeBuilder nodeBuilder () {
    return new MockNodeBuilder ();
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public HclBuilder hclBuilder () {
    return new RHclBuilder ();
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

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addJsonSerializers(java.util.List) */
  @Override
  public void addJsonSerializers (List<JsonSerializer<?>> serializers) {
    serializers.addAll (asList (new HclJsonSerializer (),
    							new HclResultJsonSerializer(),
                                new LeafJsonSerializer (),
                                new BranchJsonSerializer (),
                                new SimpleHierarchicallyClusteredDimensionJsonSerializer ()));
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addHttpMessageConverters(java.util.List) */
  @Override
  public void addHttpMessageConverters (List<HttpMessageConverter<?>> converters) {
    converters.addAll (asList (nodeNewickMessageConverter (), new HclNewickMessageConverter ()));
  }

  @Bean
  public NodeNewickMessageConverter nodeNewickMessageConverter () {
    return new NodeNewickMessageConverter ();
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addPreferredArgumentResolvers(java.util.List) */
  @Override
  public void addPreferredArgumentResolvers (List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add (new AnalysisPathVariableMethodArgumentResolver<Hcl> (Hcl.class));
  }

  @Bean
  @Rserve
  public Module registerRserveDeserializer () {
    return new SimpleModule () {
      private static final long serialVersionUID = 1L;

      {
        addDeserializer (Node.class, new NodeJsonDeserializer ());
      }
    };
  }
}
