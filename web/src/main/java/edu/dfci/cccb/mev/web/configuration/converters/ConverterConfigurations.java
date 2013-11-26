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

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author levk
 * 
 */
@Configuration
@Log4j
public class ConverterConfigurations {

  private @Inject RequestMappingHandlerAdapter requestMappingHandlerAdapter;
  private @Inject ObjectMapper jsonObjectMapper;
  private @Autowired (required = false) Collection<HttpMessageConverter<?>> converters;

  @PostConstruct
  public void registerConverters () {
    log.info ("Registering converters: " + converters);
    if (converters != null && converters.size () > 0)
      requestMappingHandlerAdapter.getMessageConverters ().addAll (0, converters);
  }

  @PostConstruct
  public void registerJacksonObjectMapper () {
    for (HttpMessageConverter<?> converter : requestMappingHandlerAdapter.getMessageConverters ())
      if (converter instanceof MappingJackson2HttpMessageConverter)
        ((MappingJackson2HttpMessageConverter) converter).setObjectMapper (jsonObjectMapper);
  }
}
