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
package edu.dfci.cccb.mev.web.configuration.resolvers;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import edu.dfci.cccb.mev.dataset.client.simple.MapHotPlugViewRegistry;
import edu.dfci.cccb.mev.dataset.client.support.freemarker.FreeMarkerViewBuilder;
import edu.dfci.cccb.mev.dataset.client.support.velocity.VelocityViewBuilder;
import edu.dfci.cccb.mev.web.support.HotPlugViewResolver;

/**
 * @author levk
 * 
 */
@Configuration
public class ViewResolverConfiguration {

  private final Map<String, View> viewMap = new HashMap<> ();

  @Bean
  public MapHotPlugViewRegistry hotPlugViewRegistry () {
    return new MapHotPlugViewRegistry (viewMap);
  }

  @Bean
  public HotPlugViewResolver hotPlugViewResolver () {
    return new HotPlugViewResolver (viewMap);
  }

  @Bean
  public FreeMarkerConfigurer freeMarkerConfigurer () {
    FreeMarkerConfigurer configurer = new FreeMarkerConfigurer ();
    configurer.setTemplateLoaderPath ("classpath:");
    configurer.setPreferFileSystemAccess (false);
    return configurer;
  }

  @Bean
  @Scope (SCOPE_PROTOTYPE)
  public FreeMarkerViewBuilder freemarkerViewBuilder () {
    return new FreeMarkerViewBuilder ();
  }

  @Bean
  @Scope (SCOPE_PROTOTYPE)
  public VelocityViewBuilder velocityViewBuilder () {
    return new VelocityViewBuilder ();
  }
}
