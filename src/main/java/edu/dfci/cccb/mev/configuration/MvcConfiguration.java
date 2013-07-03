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

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import us.levk.math.linear.util.RealMatrixJsonSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.dfci.cccb.mev.beans.Matrices;

/**
 * @author levk
 * 
 */
@Configuration
@EnableWebMvc
@ComponentScan ("edu.dfci.cccb.mev")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

  /**
   * Creates the session scoped matrix holding bean
   * 
   * @return
   */
  // TODO: Move this out to a different config file to prepare to separate
  // heatmaps into its own module
  @Bean
  @Scope (value = SCOPE_SESSION, proxyMode = TARGET_CLASS)
  public Matrices matrices () {
    return new Matrices ();
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
              .parameterName ("mediaType")
              .ignoreAcceptHeader (true)
              .useJaf (false)
              .defaultContentType (TEXT_HTML)
              .mediaType ("html", TEXT_HTML)
              .mediaType ("xml", APPLICATION_XML)
              .mediaType ("json", APPLICATION_JSON);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #configureMessageConverters(java.util.List) */
  // TODO: This should go into a separate config for heatmaps
  @Override
  public void configureMessageConverters (List<HttpMessageConverter<?>> converters) {
    converters.add (new MappingJackson2HttpMessageConverter () {
      {
        setObjectMapper (new ObjectMapper ().registerModule (new SimpleModule ().addSerializer (RealMatrix.class,
                                                                                                new RealMatrixJsonSerializer ())));
      }
    });
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #addResourceHandlers
   * (org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry) */
  @Override
  public void addResourceHandlers (ResourceHandlerRegistry registry) {
    registry.addResourceHandler ("/resources/static/**").addResourceLocations ("classpath:/META-INF/resources/",
                                                                               "/META-INF/resources/");
  }
}
