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

import static edu.dfci.cccb.mev.dataset.domain.contract.Analysis.VALID_ANALYSIS_NAME_REGEX;

import java.lang.reflect.Method;

import javax.inject.Inject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.web.context.request.NativeWebRequest;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;

/**
 * @author levk
 * 
 */
@SuppressWarnings ("unchecked")
@ToString
@EqualsAndHashCode (callSuper = false)
public class AnalysisPathVariableMethodArgumentResolver <T extends Analysis> extends AbstractTypedPathVariableMethodArgumentResolver<T> {

  public static final String ANALYSIS_MAPPING_NAME = "analysis";
  public static final String ANALYSIS_URL_ELEMENT = "{" + ANALYSIS_MAPPING_NAME + ":"
                                                    + VALID_ANALYSIS_NAME_REGEX + "}";

  private @Getter final Class<T> analysisType;
  private @Getter @Setter @Inject DatasetPathVariableMethodArgumentResolver datasetResolver;

  public AnalysisPathVariableMethodArgumentResolver (Class<T> analysisType) {
    super (analysisType, ANALYSIS_MAPPING_NAME);
    this.analysisType = analysisType;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveValue(java.lang.String, org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public T resolveObject (String value, Method method, NativeWebRequest request) throws Exception {
    return (T) datasetResolver.resolveObject (method, request).analyses ().get (value);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveValue(java.lang.String,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public T resolveObject (String value, NativeWebRequest request) throws Exception {
    return resolveAnalysis (datasetResolver.resolveObject (request), value);
  }

  private T resolveAnalysis (Dataset dataset, String name) throws AnalysisNotFoundException {
    return (T) dataset.analyses ().get (name);
  }
}
