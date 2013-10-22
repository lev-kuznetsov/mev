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
package edu.dfci.cccb.mev.heatmap.support;

import static org.springframework.web.bind.annotation.ValueConstants.DEFAULT_NONE;
import lombok.experimental.ExtensionMethod;
import lombok.extern.log4j.Log4j;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import us.levk.util.runtime.support.Annotations;
import us.levk.util.runtime.support.Classes;
import us.levk.util.runtime.support.Methods;
import ch.lambdaj.Lambda;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;

/**
 * @author levk
 * 
 */
@ExtensionMethod ({ StringUtils.class, Lambda.class })
@Log4j
public class PathVariableHeatmapMethodArgumentResolver extends PathVariableMethodArgumentResolver {

  private Workspace workspace;

  public PathVariableHeatmapMethodArgumentResolver (Workspace workspace) {
    this.workspace = workspace;
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (final MethodParameter parameter) {
    if (log.isDebugEnabled ())
      log.debug ("Method parameter "
                 + (Annotations.validate (parameter.getParameterAnnotation (PathVariable.class), "value")
                    && Classes.implementing (parameter.getParameterType (), Heatmap.class) ? "" : "not ")
                 + "supported on parameter index " + parameter.getParameterIndex () + " for "
                 + Methods.brief (parameter.getMethod ()));
    return Annotations.validate (parameter.getParameterAnnotation (PathVariable.class), "value")
           && Classes.implementing (parameter.getParameterType (), Heatmap.class);
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
    if (log.isDebugEnabled ())
      log.debug ("Resolving name "
                 + name + " with overriden implementation resolving to \""
                 + super.resolveName (name, parameter, request) + "\" on parameter "
                 + Methods.brief (parameter.getMethod ()));
    return workspace.get ((String) super.resolveName (name, parameter, request));
  }
}
