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
package edu.dfci.cccb.mev.heatmap.server.resolvers;

import static edu.dfci.cccb.mev.heatmap.domain.Dimension.from;
import static edu.dfci.cccb.mev.heatmap.server.resolvers.MethodParameters.brief;
import static org.springframework.util.StringUtils.isEmpty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import edu.dfci.cccb.mev.heatmap.domain.Dimension;

/**
 * @author levk
 * 
 */
@Log4j
@ToString
@EqualsAndHashCode (callSuper = false)
public class DimensionMethodArgumentResolver extends PathVariableMethodArgumentResolver {

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    PathVariable annotation = parameter.getParameterAnnotation (PathVariable.class);
    boolean supported = annotation == null ? false
                                          : (!isEmpty (annotation.value ()) && parameter.getParameterType ()
                                                                                        .equals (Dimension.class));
    if (log.isDebugEnabled ())
      log.debug ("Method parameter " + (supported ? "" : "not ") + "supported on parameter " + brief (parameter));
    return supported;
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
    Object result = from ((String) value);
    if (log.isDebugEnabled ())
      log.debug ("Resolving path variable " + name + " bound to " + value + " to dimension " + result);
    return result;
  }
}
