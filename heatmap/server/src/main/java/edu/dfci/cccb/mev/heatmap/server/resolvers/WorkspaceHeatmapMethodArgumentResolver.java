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

import static edu.dfci.cccb.mev.heatmap.server.resolvers.MethodParameters.brief;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.web.bind.annotation.ValueConstants.DEFAULT_NONE;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;

/**
 * @author levk
 * 
 */
@Log4j
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode (callSuper = false)
public class WorkspaceHeatmapMethodArgumentResolver extends PathVariableMethodArgumentResolver {

  private final Workspace workspace;

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    PathVariable annotation = parameter.getParameterAnnotation (PathVariable.class);
    boolean supported = annotation == null ? false
                                          : (!isEmpty (annotation.value ()) && parameter.getParameterType ()
                                                                                        .equals (Heatmap.class));
    if (log.isDebugEnabled ())
      log.debug ("Method parameter " + (supported ? "" : "not ") + "supported on parameter " + brief (parameter));
    return supported;
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver
   * #createNamedValueInfo(org.springframework.core.MethodParameter) */
  @Override
  protected NamedValueInfo createNamedValueInfo (MethodParameter parameter) {
    class PathVariableHeatmapNamedValueInfo extends NamedValueInfo {
      /**
       * @param annotation
       */
      public PathVariableHeatmapNamedValueInfo (PathVariable annotation) {
        super (annotation.value (), true, DEFAULT_NONE);
      }
    }

    return new PathVariableHeatmapNamedValueInfo (parameter.getParameterAnnotation (PathVariable.class));
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver#resolveName(java.lang.String,
   * org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  protected Object resolveName (String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
    String id = (String) super.resolveName (name, parameter, request);
    if (log.isDebugEnabled ())
      log.debug ("Resolving path variable " + name + " bound to " + id
                 + " on request " + request + " for parameter " + brief (parameter));
    return workspace.get (id);
  }
}
