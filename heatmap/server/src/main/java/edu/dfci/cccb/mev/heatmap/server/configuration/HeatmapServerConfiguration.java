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
package edu.dfci.cccb.mev.heatmap.server.configuration;

import static edu.dfci.cccb.mev.heatmap.server.converters.NewickMessageConverter.NEWICK_EXTENSION;
import static edu.dfci.cccb.mev.heatmap.server.converters.NewickMessageConverter.NEWICK_MEDIA_TYPE;
import static org.springframework.context.annotation.FilterType.ANNOTATION;

import javax.inject.Inject;

import lombok.ToString;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import edu.dfci.cccb.mev.heatmap.domain.HeatmapBuilder;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;
import edu.dfci.cccb.mev.heatmap.server.converters.NewickMessageConverter;
import edu.dfci.cccb.mev.heatmap.server.resolvers.DimensionMethodArgumentResolver;
import edu.dfci.cccb.mev.heatmap.server.resolvers.ImportHeatmapMethodArgumentResolver;
import edu.dfci.cccb.mev.heatmap.server.resolvers.WorkspaceHeatmapMethodArgumentResolver;

/**
 * @author levk
 * 
 */
@Configuration
@ComponentScan (value = "edu.dfci.cccb.mev.heatmap",
                includeFilters = @Filter (type = ANNOTATION, value = {
                                                                      Controller.class,
                                                                      ControllerAdvice.class,
                                                                      RestController.class }))
@ToString
@Import (DomainConfiguration.class)
public class HeatmapServerConfiguration extends WebMvcConfigurerAdapter {

  private @Inject Workspace workspace;
  private @Inject HeatmapBuilder heatmapBuilder;
  private @Inject ConfigurableBeanFactory beanFactory;
  private @Inject NewickMessageConverter newickMessageConverter;
  private @Inject RequestMappingHandlerAdapter requestMappingHandlerAdapter;

  @Bean
  public NewickMessageConverter newickMessageConverter () {
    return new NewickMessageConverter ();
  }

  @Bean
  public WorkspaceHeatmapMethodArgumentResolver workspaceHeatmapMethodArgumentResolver (Workspace workspace) {
    return new WorkspaceHeatmapMethodArgumentResolver (workspace);
  }

  @Bean
  public ImportHeatmapMethodArgumentResolver importHeatmapMethodArgumentResolver (ConfigurableBeanFactory beanFactory,
                                                                                  HeatmapBuilder heatmapBuilder) {
    return new ImportHeatmapMethodArgumentResolver (beanFactory, false, heatmapBuilder);
  }

  @Bean
  DimensionMethodArgumentResolver dimensionMethodArgumentResolver () {
    return new DimensionMethodArgumentResolver ();
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
