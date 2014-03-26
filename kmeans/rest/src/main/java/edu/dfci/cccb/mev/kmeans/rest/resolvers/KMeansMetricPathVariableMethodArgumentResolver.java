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
package edu.dfci.cccb.mev.kmeans.rest.resolvers;

import static edu.dfci.cccb.mev.kmeans.domain.hadoop.Metric.from;

import java.lang.reflect.Method;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AbstractTypedPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.kmeans.domain.hadoop.Metric;

/**
 * @author levk
 * 
 */
@Log4j
@EqualsAndHashCode (callSuper = true)
@ToString
public class KMeansMetricPathVariableMethodArgumentResolver extends AbstractTypedPathVariableMethodArgumentResolver<Metric> {

  public static final String METRIC_MAPPING_NAME = "metric";
  public static final String METRIC_URL_ELEMENT = "{" + METRIC_MAPPING_NAME + "}";

  /**
   */
  public KMeansMetricPathVariableMethodArgumentResolver () {
    super (Metric.class, METRIC_MAPPING_NAME);
  }
  
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    return super.supportsParameter (parameter);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveObject(java.lang.String, org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public Metric resolveObject (String value, Method method, NativeWebRequest request) {
    if (log.isDebugEnabled ())
      log.debug ("resolveObject: " + value + "," + method);
    return from (value);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveObject(java.lang.String,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public Metric resolveObject (String value, NativeWebRequest request) {
    if (log.isDebugEnabled ())
      log.debug ("resolveObject: " + value);
    return from (value);
  }
}
