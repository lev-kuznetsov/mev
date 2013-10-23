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
package edu.dfci.cccb.mev.core.configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

import us.levk.spring.web.view.BeanMapHotPlugViewResolver;

/**
 * @author levk
 * 
 */
@Configuration
@ComponentScan (value = "edu.dfci.cccb.mev.core",
                useDefaultFilters = false,
                includeFilters = @Filter ({ Controller.class, ControllerAdvice.class }))
public class CoreConfiguration {

  private @Inject BeanMapHotPlugViewResolver viewResolver;

  @PostConstruct
  public void registerViews () {
    viewResolver.addAnnotatedClasses (CoreViews.class);
  }
}
