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
package edu.dfci.cccb.mev.heatmap.configuration;

import java.util.List;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.XmlViewResolver;

import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;
import edu.dfci.cccb.mev.heatmap.support.AreaMethodArgumentResolver;
import edu.dfci.cccb.mev.heatmap.support.AxisMethodArgumentResolver;
import edu.dfci.cccb.mev.heatmap.support.PathVariableHeatmapMethodArgumentResolver;
import edu.dfci.cccb.mev.heatmap.support.RequestBodyHeatmapMethodArgumentResolver;

/**
 * @author levk
 * 
 */
@Log4j
@Configuration
@ComponentScan (basePackages = "edu.dfci.cccb.mev.heatmap",
                useDefaultFilters = false,
                includeFilters = @Filter ({ Controller.class, ControllerAdvice.class }))
public class HeatmapConfiguration extends WebMvcConfigurerAdapter {

  {
    log.info ("Loading configuration for heatmap plugin");
  }
  
  @Bean
  public XmlViewResolver heatmapXmlViewResolver () {
    return new XmlViewResolver () {
      {
        setLocation (new ClassPathResource ("/edu/dfci/cccb/mev/heatmap/views/views.xml"));
        setOrder (LOWEST_PRECEDENCE);
      }
    };
  }

  // FIXME: stub
  @Bean
  public Workspace workspace () {
    return null;
  }

  // FIXME: stub
  @Bean
  public Heatmap.Builder builder () {
    return null;
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #addArgumentResolvers(java.util.List) */
  @Override
  public void addArgumentResolvers (List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add (new AreaMethodArgumentResolver ());
    argumentResolvers.add (new AxisMethodArgumentResolver ());
    argumentResolvers.add (new PathVariableHeatmapMethodArgumentResolver (workspace ()));
    argumentResolvers.add (new RequestBodyHeatmapMethodArgumentResolver (builder ()));
  }
}
