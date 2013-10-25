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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;
import static us.levk.spring.web.view.Views.freemarker;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.ToString;
import lombok.experimental.ExtensionMethod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import us.levk.spring.web.view.BeanMapHotPlugViewResolver;
import us.levk.spring.web.view.Views.AbstractViewBuilder.AbstractUrlBasedViewBuilder.AbstractTemplateViewBuilder.FreeMarkerViewBuilder;
import us.levk.util.runtime.support.Classes;
import ch.lambdaj.Lambda;

/**
 * @author levk
 * 
 */
@EnableWebMvc
@Configuration
@ExtensionMethod ({ Collections.class, Lambda.class, Arrays.class, Classes.class })
@ToString
public class DispatcherConfiguration extends WebMvcConfigurerAdapter {

  private @Inject Environment environment;
  private @Inject WebApplicationContext context;

  /**
   * Default maximum upload size for multipart resolver if
   * edu.dfci.cccb.mev.configuration.DispatcherConfiguration.MAX_UPLOAD_SIZE
   * property is not defined in the environment; default is 20 gigabytes
   */
  private static final long DEFAULT_MAX_UPLOAD_SIZE = 1024L * 1024L * 1024L * 20L;

  /**
   * Create the CNVR
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
   */
  @Bean
  public CommonsMultipartResolver multipartResolver () {
    return new CommonsMultipartResolver () {
      {
        setMaxUploadSize (environment.getProperty ("multipart.upload.maximum.size",
                                                   Long.class,
                                                   DEFAULT_MAX_UPLOAD_SIZE));
        setResolveLazily (true);
      }
    };
  }

  /**
   * FTL configuration
   */
  @Bean
  public FreeMarkerConfigurer freeMarkerConfiguration () {
    return new FreeMarkerConfigurer () {
      {
        setTemplateLoaderPath ("classpath:");
        setPreferFileSystemAccess (false);
      }
    };
  }

  /**
   * FTL view builder
   */
  @Bean
  public FreeMarkerViewBuilder freeMarkerViewBuilder () {
    return freemarker ();
  }

  /**
   * Hot plug view resolver, plugins will autowire this bean and inject views
   * during {@link PostConstruct}
   */
  @Bean
  public BeanMapHotPlugViewResolver beanMapHotPlugViewResolver () {
    return new BeanMapHotPlugViewResolver ();
  }

  @Bean
  public Map<String, Collection<InputStreamSource>> javascriptSourceHotPlugMap () {
    return new ConcurrentHashMap<String, Collection<InputStreamSource>> () {
      private static final long serialVersionUID = 1L;

      /* (non-Javadoc)
       * @see java.util.concurrent.ConcurrentHashMap#get(java.lang.Object)
       */
      @Override
      public Collection<InputStreamSource> get (Object key) {
        return putIfAbsent ((String) key, new LinkedList<InputStreamSource> ());
      }
    };
  }

  @Bean
  public Resource[] application () {
    return new Resource[] { new ClassPathResource ("/edu/dfci/cccb/mev/core/javascript/application.js") };
  }

  @Bean
  public Resource[] heatmap () {
    return new Resource[] { new ClassPathResource ("/edu/dfci/cccb/mev/heatmap/javascript/heatmap.js") };
  }

  /**
   * JSON view resolver
   */
  @Bean
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
              .mediaType ("json", APPLICATION_JSON);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #addResourceHandlers
   * (org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry) */
  @Override
  public void addResourceHandlers (ResourceHandlerRegistry registry) {
    registry.addResourceHandler ("/resources/library/**").addResourceLocations ("classpath:/META-INF/resources/webjars/");
    registry.addResourceHandler ("/resources/heatmap/**").addResourceLocations ("classpath:/edu/dfci/cccb/mev/heatmap/javascript/");
    /*
     *../heatmap/javascript/injector.js
     * 
     */
  }
}
