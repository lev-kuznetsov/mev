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
package edu.dfci.cccb.mev.kmeans.rest.configuration;

import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.List;

import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;

import com.fasterxml.jackson.databind.JsonSerializer;

import edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeansBuilder;
import edu.dfci.cccb.mev.kmeans.domain.r.DispatchedRKMeansBuilder;
import edu.dfci.cccb.mev.kmeans.rest.assembly.json.KMeansJsonSerializer;
import edu.dfci.cccb.mev.kmeans.rest.assembly.tsv.KMeansTsvMessageConverter;

/**
 * @author levk
 * 
 */
@Configuration
@ToString
@ComponentScan (basePackages = "edu.dfci.cccb.mev.kmeans.rest.controllers")
public class KMeansRestConfiguration extends MevRestConfigurerAdapter {

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public KMeansBuilder kMeansBuilder () {
    return new DispatchedRKMeansBuilder ();
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addJsonSerializers(java.util.List) */
//  @Override
//  public void addJsonSerializers (List<JsonSerializer<?>> serializers) {
//    serializers.add (new KMeansJsonSerializer ());
//  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.rest.prototype.MevRestConfigurerAdapter
   * #addHttpMessageConverters(java.util.List) */
  @Override
  public void addHttpMessageConverters (List<HttpMessageConverter<?>> converters) {
    converters.add (new KMeansTsvMessageConverter ());
  }
}
