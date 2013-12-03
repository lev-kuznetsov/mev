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

import static edu.dfci.cccb.mev.hcl.rest.converters.NewickMessageConverter.NEWICK_EXTENSION;
import static edu.dfci.cccb.mev.hcl.rest.converters.NewickMessageConverter.NEWICK_MEDIA_TYPE;
import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.hcl.domain.concrete.TwoDimensionalHcl;
import edu.dfci.cccb.mev.hcl.domain.contract.Hcl;
import edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder;
import edu.dfci.cccb.mev.hcl.domain.mock.MockNodeBuilder;
import edu.dfci.cccb.mev.hcl.rest.converters.NewickMessageConverter;

/**
 * @author levk
 * 
 */
@Configuration
@ToString
@ComponentScan (basePackages = "edu.dfci.cccb.mev.hcl.rest.controllers")
public class HclConfiguration extends WebMvcConfigurerAdapter {

  @Bean
  public NodeBuilder nodeBuilder () {
    return new MockNodeBuilder ();
  }

  @Bean
  public Hcl hcl (NodeBuilder nodeBuilder) {
    return new TwoDimensionalHcl (nodeBuilder);
  }

  @Bean
  public NewickMessageConverter newickMessageConverter () {
    return new NewickMessageConverter ();
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
