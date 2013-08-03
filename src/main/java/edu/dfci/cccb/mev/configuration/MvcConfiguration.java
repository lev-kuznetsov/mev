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
package edu.dfci.cccb.mev.configuration;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.TEXT_PLAIN;

import java.util.Locale;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * @author levk
 * 
 */
@Configuration
@EnableWebMvc
@ComponentScan ("edu.dfci.cccb.mev")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

  /**
   * Loads build properties as an application accessible bean
   * 
   * @return
   */
  @Bean (name = "buildProperties")
  public PropertiesFactoryBean globalBuildProperties () {
    return new PropertiesFactoryBean () {
      {
        setLocation (new ClassPathResource ("build.properties"));
      }
    };
  }

  /**
   * Create the CNVR. Get Spring to inject the ContentNegotiationManager created
   * by the configurer (see previous method).
   */
  @Bean
  public ViewResolver contentNegotiatingViewResolver (final ContentNegotiationManager manager) {
    return new ContentNegotiatingViewResolver () {
      {
        setContentNegotiationManager (manager);
      }
    };
  }

  /**
   * Multipart resolver for file upload
   * 
   * @return
   */
  @Bean
  public CommonsMultipartResolver multipartResolver () {
    return new CommonsMultipartResolver () {
      {
        setMaxUploadSize (10000000);
      }
    };
  }

  @Bean (name = "jspViewResolver")
  public ViewResolver jspViewResolver () {
    return new InternalResourceViewResolver () {
      {
        setPrefix ("META-INF/resources/mev/views/");
        setSuffix (".jsp");
        setOrder (2);
        setExposedContextBeanNames (new String[] { "buildProperties" });
      }
    };
  }

  @Bean (name = "jsonViewResolver")
  public ViewResolver jsonViewResolver () {
    return new ViewResolver () {

      @Override
      public View resolveViewName (String viewName, Locale locale) throws Exception {
        return new MappingJackson2JsonView () {
          {
            setPrettyPrint (true);
          }
        };
      }
    };
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #configureContentNegotiation
   * (org.springframework.web.servlet.config.annotation
   * .ContentNegotiationConfigurer) */
  @Override
  public void configureContentNegotiation (ContentNegotiationConfigurer configurer) {
    configurer.favorPathExtension (true)
              .favorParameter (true)
              .parameterName ("format")
              .ignoreAcceptHeader (true)
              .useJaf (false)
              .defaultContentType (TEXT_HTML)
              .mediaType ("html", TEXT_HTML)
              .mediaType ("xml", APPLICATION_XML)
              .mediaType ("tsv", TEXT_PLAIN)
              .mediaType ("json", APPLICATION_JSON);
  }

  @Override
  public void addResourceHandlers (ResourceHandlerRegistry registry) {
    registry.addResourceHandler ("/resources/static/**").addResourceLocations ("classpath:/META-INF/resources/",
                                                                               "/META-INF/resources/");
  }
}
