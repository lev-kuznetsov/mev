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
package edu.dfci.cccb.mev.dataset.rest.configuration;

import lombok.ToString;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import edu.dfci.cccb.mev.dataset.rest.assembly.json.DatasetJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.assembly.tsv.MultipartUploadDatasetArgumentResolver;
import edu.dfci.cccb.mev.dataset.rest.context.DatasetBuilderConfiguration;
import edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector;

/**
 * @author levk
 * 
 */
@Configuration
@Import ({ DatasetBuilderConfiguration.class, RestPathVariableDatasetRequestContextInjector.class })
@ComponentScan (basePackages = "edu.dfci.cccb.mev.dataset.rest.controllers")
@ToString
public class DatasetRestConfiguration {

  @Bean
  public MultipartUploadDatasetArgumentResolver multipartUploadDatasetArgumentResolver (ConfigurableBeanFactory beanFactory) {
    return new MultipartUploadDatasetArgumentResolver (beanFactory, false);
  }

  @Bean
  public DatasetJsonSerializer datasetJsonSerializer () {
    return new DatasetJsonSerializer ();
  }
}
