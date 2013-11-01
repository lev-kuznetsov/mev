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

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import edu.dfci.cccb.mev.api.client.support.injectors.ViewRegistrar;
import edu.dfci.cccb.mev.web.support.HotPlugViewResolver;
import edu.dfci.cccb.mev.web.support.MappedHotPlugViewRegistry;

/**
 * @author levk
 * 
 */
@Configuration
public class ViewResolverConfiguration {

  private final Map<String, View> views = new HashMap<String, View> () {
    private static final long serialVersionUID = 1L;

    public View put (String key, View value) {
      return super.put (key, value);
    }
  };

  @Bean
  public ViewRegistrar hotPlugViewRegistry () {
    return new MappedHotPlugViewRegistry (views);
  }

  @Bean
  public ViewResolver hotPlugViewResolver () {
    return new HotPlugViewResolver (views);
  }

  @Bean
  public FreeMarkerConfigurer freeMarkerConfigurer () {
    FreeMarkerConfigurer configurer = new FreeMarkerConfigurer ();
    configurer.setTemplateLoaderPath ("classpath:");
    configurer.setPreferFileSystemAccess (false);
    return configurer;
  }
}
