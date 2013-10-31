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
package edu.dfci.cccb.mev.heatmap.client.configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.api.client.support.injectors.ViewRegistrar;
import edu.dfci.cccb.mev.api.client.support.injectors.InjectorRegistry;

/**
 * @author levk
 * 
 */
@Configuration
@Log4j
public class HeatmapClientConfiguration extends WebMvcConfigurerAdapter {

  private @Inject ViewRegistrar views;
  private @Inject InjectorRegistry injectors;

  @PostConstruct
  public void registerViews () {
    log.debug ("Registering views in " + Views.class.getName () + " with " + views);
    views.registerAnnotatedViewBeanClasses (Views.class);
  }

  @PostConstruct
  public void registerInjectors () {
    injectors.add ("/heatmap/javascript/main.js");
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #addResourceHandlers
   * (org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry) */
  @Override
  public void addResourceHandlers (ResourceHandlerRegistry registry) {
    log.debug ("Adding resource handlers for heatmap plugin");
    registry.addResourceHandler ("/heatmap/javascript/**")
            .addResourceLocations ("classpath:/edu/dfci/cccb/mev/heatmap/client/javascript/");
    registry.addResourceHandler ("/heatmap/style/**")
            .addResourceLocations ("classpath:/edu/dfci/cccb/mev/heatmap/client/style/");
  }
}
