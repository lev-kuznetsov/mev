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
package edu.dfci.cccb.mev.web.configuration.container;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import edu.dfci.cccb.mev.api.client.support.injectors.InjectorRegistry;
import edu.dfci.cccb.mev.api.client.support.injectors.ViewRegistrar;
import edu.dfci.cccb.mev.web.domain.reflection.Reflection;
import edu.dfci.cccb.mev.web.domain.reflection.concrete.SpringReflector;

/**
 * @author levk
 * 
 */
@Configuration
@ComponentScan (basePackages = "edu.dfci.cccb.mev.web",
                excludeFilters = @Filter (type = ANNOTATION, value = Configuration.class),
                includeFilters = @Filter (type = ANNOTATION, value = { Controller.class, ControllerAdvice.class }))
public class ContainerConfigurations extends WebMvcConfigurerAdapter {

  private @Inject ViewRegistrar views;
  private @Inject InjectorRegistry injectors;

  @Bean
  public Reflection reflection () {
    return new SpringReflector ();
  }

  @PostConstruct
  public void registerViews () {
    views.registerAnnotatedViewBeanClasses (Views.class);
  }

  @PostConstruct
  public void injectJavaScript () {
    injectors.add ("/container/javascript/main.js");
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #addResourceHandlers
   * (org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry) */
  @Override
  public void addResourceHandlers (ResourceHandlerRegistry registry) {
    registry.addResourceHandler ("/container/javascript/**")
            .addResourceLocations ("classpath:/edu/dfci/cccb/mev/web/javascript/");
  }
}
