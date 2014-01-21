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
package edu.dfci.cccb.mev.web.configuration.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.configuration.rest.contract.HttpMessageConverterConfigurer;

/**
 * @author levk
 * 
 */
@Configuration
@Log4j
public class ConverterConfigurations {

  private @Inject RequestMappingHandlerAdapter requestMappingHandlerAdapter;
  private @Inject ObjectMapper jsonObjectMapper;
  private @Autowired (required = false) Collection<HttpMessageConverterConfigurer> converterConfigurers;
  private @Inject AutowireCapableBeanFactory beanFactory;

  @PostConstruct
  public void registerConverters () {
    List<HttpMessageConverter<?>> converters = new ArrayList<> ();
    if (converterConfigurers != null) {
      List<HttpMessageConverter<?>> configurerConverters = new ArrayList<> ();
      for (HttpMessageConverterConfigurer configurer : converterConfigurers)
        configurer.addHttpMessageConverters (configurerConverters);
      for (HttpMessageConverter<?> converter : configurerConverters)
        beanFactory.autowireBean (converter);
      converters.addAll (configurerConverters);
    }
    log.info ("Registering converters: " + converters);
    requestMappingHandlerAdapter.getMessageConverters ().addAll (0, converters);
  }

  @PostConstruct
  public void registerJacksonObjectMapper () {
    for (HttpMessageConverter<?> converter : requestMappingHandlerAdapter.getMessageConverters ())
      if (converter instanceof MappingJackson2HttpMessageConverter)
        ((MappingJackson2HttpMessageConverter) converter).setObjectMapper (jsonObjectMapper);
  }
}
