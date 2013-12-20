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

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import java.lang.reflect.Method;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j;

import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import edu.dfci.cccb.mev.dataset.rest.configuration.MissingPathVariableException;

/**
 * @author levk
 * 
 */
@SuppressWarnings ("unchecked")
@RequiredArgsConstructor
@Log4j
public abstract class AbstractTypedPathVariableMethodArgumentResolver <T> extends PathVariableMethodArgumentResolver implements Ordered {

  private final @Getter Class<T> supportedType;
  private final @Getter String defaultName;
  private @Getter @Setter int order = LOWEST_PRECEDENCE;

  public abstract T resolveObject (String value, Method method, NativeWebRequest request) throws Exception;

  public abstract T resolveObject (String value, NativeWebRequest request) throws Exception;

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    return super.supportsParameter (parameter) && supportedType.isAssignableFrom (parameter.getParameterType ());
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver#resolveName(java.lang.String,
   * org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  @Synchronized
  protected Object resolveName (String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
    String value = resolveValue (name, request);
    if (value == null)
      throw new MissingPathVariableException ().name (name);
    return resolveObject ((String) value, parameter.getMethod (), request);
  }

  public T resolveObject (Method method, NativeWebRequest request) throws Exception {
    if (method != null) {
      for (int index = method.getParameterTypes ().length; --index >= 0;) {
        MethodParameter parameter = new MethodParameter (method, index);
        if (supportsParameter (parameter))
          return (T) resolveName (parameter.getParameterAnnotation (PathVariable.class).value (), parameter, request);
      }
    }
    return resolveObject (resolveValue (defaultName, request), request);
  }

  public T resolveObject (NativeWebRequest request) throws Exception {
    return resolveObject (resolveValue (defaultName, request), request);
  }

  protected String resolveValue (String name, NativeWebRequest request) {
    String result = ((Map<String, String>) request.getAttribute (URI_TEMPLATE_VARIABLES_ATTRIBUTE,
                                                                 SCOPE_REQUEST)).get (name);
    if (log.isDebugEnabled ())
      log.debug ("Resolving " + name + " to " + result + " for resolver "
                 + getClass ().getSimpleName () + " with mappings "
                 + request.getAttribute (URI_TEMPLATE_VARIABLES_ATTRIBUTE,
                                         SCOPE_REQUEST));
    return result;
  }
}
