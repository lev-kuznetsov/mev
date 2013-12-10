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

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.from;

import javax.inject.Inject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;

/**
 * @author levk
 * 
 */
@ToString (exclude = "dataset")
@EqualsAndHashCode (callSuper = false)
public class DimensionPathVariableMethodArgumentResolver extends PathVariableMethodArgumentResolver implements Ordered {

  private @Getter @Setter (onMethod = @_ (@Inject)) Dataset dataset;
  private @Getter @Setter int order = LOWEST_PRECEDENCE;

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    return Dimension.class.isAssignableFrom (parameter.getParameterType ()) && super.supportsParameter (parameter);
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver#resolveName(java.lang.String,
   * org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  protected Object resolveName (String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
    Object value = super.resolveName (name, parameter, request);
    if (value == null)
      return null;
    return dataset.dimension (from (value.toString ()));
  }
}
