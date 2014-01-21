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
package edu.dfci.cccb.mev.configuration.rest.prototype;

import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.configuration.rest.contract.HandlerMethodArgumentResolverConfigurer;
import edu.dfci.cccb.mev.configuration.rest.contract.HttpMessageConverterConfigurer;
import edu.dfci.cccb.mev.configuration.rest.contract.JacksonConfigurer;
import edu.dfci.cccb.mev.configuration.rest.contract.MultipartConfigurer;

/**
 * @author levk
 * 
 */
public abstract class MevRestConfigurerAdapter extends WebMvcConfigurerAdapter
        implements HandlerMethodArgumentResolverConfigurer, MultipartConfigurer,
        HttpMessageConverterConfigurer, JacksonConfigurer {

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.rest.contract.HandlerMethodArgumentResolverConfigurer
   * #addPreferredArgumentResolvers(java.util.List) */
  @Override
  public void addPreferredArgumentResolvers (List<HandlerMethodArgumentResolver> resolvers) {}

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.rest.contract.HttpMessageConverterConfigurer
   * #addHttpMessageConverters(java.util.List) */
  @Override
  public void addHttpMessageConverters (List<HttpMessageConverter<?>> converters) {}

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.rest.contract.JacksonConfigurer#addJsonSerializers
   * (java.util.List) */
  @Override
  public void addJsonSerializers (List<JsonSerializer<?>> serializers) {}

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.rest.contract.JacksonConfigurer#configureMapper
   * (com.fasterxml.jackson.databind.ObjectMapper) */
  @Override
  public void configureObjectMapper (ObjectMapper mapper) {}

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.contract.MultipartConfigurer#
   * configureMultipartResolver
   * (org.springframework.web.multipart.MultipartResolver) */
  @Override
  public void configureMultipartResolver (MultipartResolver resolver) {}
}
