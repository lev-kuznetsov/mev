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
package edu.dfci.cccb.mev.web.configuration;

import static edu.dfci.cccb.mev.web.context.MevContextHolder.holder;
import static java.util.Arrays.copyOf;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import edu.dfci.cccb.mev.api.client.annotation.Client.Static;
import edu.dfci.cccb.mev.api.client.annotation.ClientContext;
import edu.dfci.cccb.mev.api.server.annotation.ServerContext;
import edu.dfci.cccb.mev.web.support.JsonViewResolver;

/**
 * @author levk
 * 
 */
public class DispatcherConfiguration extends WebMvcConfigurerAdapter {

  private @Inject Environment environment;

  @Bean
  public MultipartResolver multipartResolver () {
    final long DEFAULT_MAX_UPLOAD_SIZE = 1024L * 1024L * 1024L * 20L; // 20Gb

    CommonsMultipartResolver resolver = new CommonsMultipartResolver ();
    resolver.setMaxUploadSize (environment.getProperty ("multipart.upload.max.size",
                                                        Long.class,
                                                        DEFAULT_MAX_UPLOAD_SIZE));
    resolver.setResolveLazily (true);
    return resolver;
  }

  @Bean
  public ViewResolver contentNegotiatingViewResolver (final ContentNegotiationManager manager) {
    ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver ();
    resolver.setContentNegotiationManager (manager);
    return resolver;
  }

  // TODO: freemarker view resolver

  @Bean
  public ViewResolver jsonViewResolver () {
    return new JsonViewResolver ();
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

  @Bean
  public FreeMarkerConfigurer freeMarkerConfigurer () {
    FreeMarkerConfigurer configurer = new FreeMarkerConfigurer ();
    configurer.setTemplateLoaderPath ("classpath:");
    configurer.setPreferFileSystemAccess (false);
    return configurer;
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #addResourceHandlers
   * (org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry) */
  @Override
  public void addResourceHandlers (ResourceHandlerRegistry registry) {
    for (Static resources : holder ().client ().resources ()) {
      String[] classpath = copyOf (resources.classpath (), resources.classpath ().length);
      for (int index = classpath.length; --index >= 0; classpath[index] = "classpath:" + classpath[index]);
      registry.addResourceHandler (resources.export ()).addResourceLocations (classpath);
    }
  }

  @Bean
  public ServerContext serverContext () {
    return holder ().server ();
  }

  @Bean
  ClientContext clientContext () {
    return holder ().client ();
  }
}
