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

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import us.levk.math.linear.util.RealMatrixJsonSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author levk
 * 
 */
@Configuration
@EnableWebMvc
@ComponentScan ("edu.dfci.cccb.mev")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

  /* @Bean public ViewResolver negotiatingViewResolver () { return new
   * ContentNegotiatingViewResolver () { { setContentNegotiationManager (new
   * ContentNegotiationManager (new PathExtensionContentNegotiationStrategy (new
   * HashMap<String, MediaType> () { private static final long serialVersionUID
   * = 1L; { put ("json", APPLICATION_JSON); put ("xml", APPLICATION_XML); }
   * }))); setDefaultViews (asList ((View) new MappingJackson2JsonView (),
   * (View) new MarshallingView (new Jaxb2Marshaller () { { setPackagesToScan
   * (new String[] { "edu.dfci.cccb.mev" }); } }))); setOrder (1); } }; }
   * @Bean public ViewResolver defaultViewResolver () { return new
   * InternalResourceViewResolver () { { setViewClass (JstlView.class);
   * setSuffix (".jsp"); setPrefix ("/WEB-INF/views/"); setOrder (0); } }; } */

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

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #configureContentNegotiation
   * (org.springframework.web.servlet.config.annotation
   * .ContentNegotiationConfigurer) */
  @Override
  public void configureContentNegotiation (ContentNegotiationConfigurer configurer) {
    super.configureContentNegotiation (configurer);
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
  @Override
  public void configureMessageConverters (List<HttpMessageConverter<?>> converters) {
    super.configureMessageConverters (converters);
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
    super.addResourceHandlers (registry);
    registry.addResourceHandler ("/resources/static/**").addResourceLocations ("classpath:/META-INF/resources/",
                                                                               "/META-INF/resources/");
  }
}
