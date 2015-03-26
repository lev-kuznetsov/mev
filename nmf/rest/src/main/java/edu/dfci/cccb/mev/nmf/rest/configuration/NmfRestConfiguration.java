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
package edu.dfci.cccb.mev.nmf.rest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AnalysisPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.nmf.domain.Nmf;
import edu.dfci.cccb.mev.nmf.domain.NmfBuilder;

/**
 * @author levk
 * 
 */
@Configuration
@ComponentScan ("edu.dfci.cccb.mev.nmf.rest.controllers")
public class NmfRestConfiguration {

  @Bean
  @Scope ("prototype")
  public NmfBuilder nmfBuilder () {
    return new NmfBuilder ();
  }

  @Bean
  public AnalysisPathVariableMethodArgumentResolver<Nmf> nmfAnalysisPathVariableMethodArgumentResolver () {
    return new AnalysisPathVariableMethodArgumentResolver<> (Nmf.class);
  }
}
