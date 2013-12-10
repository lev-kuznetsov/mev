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

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_REQUEST_ATTRIBUTE_NAME;
import static java.lang.Integer.MAX_VALUE;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;

/**
 * @author levk
 * 
 */
@ToString
@EqualsAndHashCode (callSuper = false)
public class AnalysisPathVariableMethodArgumentResolver <T extends Analysis> extends PathVariableMethodArgumentResolver implements Ordered {

  private @Getter final Class<T> analysisType;
  private @Getter @Setter int order = MAX_VALUE;

  public AnalysisPathVariableMethodArgumentResolver (Class<T> analysisType) {
    this.analysisType = analysisType;
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.method.annotation.
   * PathVariableMethodArgumentResolver
   * #supportsParameter(org.springframework.core.MethodParameter) */
  @Override
  public boolean supportsParameter (MethodParameter parameter) {
    return analysisType.isAssignableFrom (parameter.getParameterType ()) && super.supportsParameter (parameter);
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
    Analysis analysis = ((Dataset) request.getAttribute (DATASET_REQUEST_ATTRIBUTE_NAME,
                                                         RequestAttributes.SCOPE_REQUEST)).analyses ()
                                                                                          .get ((String) value);
    if (analysisType.isInstance (analysis))
      return analysis;
    else
      throw new ClassCastException (); // TODO: correct exception
  }
}
