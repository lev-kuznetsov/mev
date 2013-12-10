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
package edu.dfci.cccb.mev.dataset.rest.resolvers;

import java.util.Arrays;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.aop.scope.ScopedObject;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;

/**
 * @author levk
 * 
 */
@Log4j
@ToString (exclude = "dataset")
public class DatasetPathVariableMethodArgumentResolver extends PathVariableMethodArgumentResolver implements Ordered {

  public static final String DATASET_REQUEST_ATTRIBUTE_NAME = "mev.dataset";

  private @Getter @Setter (onMethod = @_ (@Inject)) Dataset dataset;
  private @Getter @Setter int order = LOWEST_PRECEDENCE - 1;

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    return Dataset.class.isAssignableFrom (parameter.getParameterType ()) && super.supportsParameter (parameter);
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver#resolveName(java.lang.String,
   * org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  protected Object resolveName (String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
    if (log.isDebugEnabled ())
      log.debug ("Resolved " + dataset + " of type " + dataset.getClass () + " implementing "
                 + Arrays.toString (dataset.getClass ().getInterfaces ()));
    request.setAttribute (DATASET_REQUEST_ATTRIBUTE_NAME,
                          dataset instanceof ScopedObject ? ((ScopedObject) dataset).getTargetObject () : dataset,
                          RequestAttributes.SCOPE_REQUEST);
    return dataset;
  }
}
