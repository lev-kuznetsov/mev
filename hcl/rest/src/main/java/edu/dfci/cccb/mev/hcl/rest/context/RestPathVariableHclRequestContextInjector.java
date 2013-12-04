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
package edu.dfci.cccb.mev.hcl.rest.context;

import static edu.dfci.cccb.mev.api.server.support.PathVariables.variable;
import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.NativeWebRequest;

import edu.dfci.cccb.mev.api.server.support.MissingPathVariableException;
import edu.dfci.cccb.mev.hcl.domain.contract.Algorithm;
import edu.dfci.cccb.mev.hcl.domain.contract.InvalidAlgorithmException;
import edu.dfci.cccb.mev.hcl.domain.contract.InvalidMetricException;
import edu.dfci.cccb.mev.hcl.domain.contract.Metric;

/**
 * @author levk
 * 
 */
@Configuration
public class RestPathVariableHclRequestContextInjector {

  private static final String METRIC = "metric";
  private static final String ALGORITHM = "algorithm";

  public static final String METRIC_URL_ELEMENT = "{" + METRIC + "}";
  public static final String ALGORITHM_URL_ELEMENT = "{" + ALGORITHM + "}";

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Metric metric (Collection<Metric> metrics, NativeWebRequest request) throws InvalidMetricException,
                                                                             MissingPathVariableException {
    String name = variable (METRIC, request);
    for (Metric metric : metrics)
      if (metric.name ().equals (name))
        return metric;
    throw new InvalidMetricException ().name (name);
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Algorithm algorithm (Collection<Algorithm> algorithms, NativeWebRequest request) throws InvalidAlgorithmException,
                                                                                         MissingPathVariableException {
    String name = variable (METRIC, request);
    for (Algorithm algorithm : algorithms)
      if (algorithm.name ().equals (name))
        return algorithm;
    throw new InvalidAlgorithmException ().name (name);
  }
}
