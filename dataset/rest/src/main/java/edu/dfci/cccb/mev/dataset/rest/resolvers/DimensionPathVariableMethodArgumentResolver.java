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

import java.lang.reflect.Method;

import javax.inject.Inject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.web.context.request.NativeWebRequest;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;

/**
 * @author levk
 * 
 */
@ToString
@EqualsAndHashCode (callSuper = false)
public class DimensionPathVariableMethodArgumentResolver extends AbstractTypedPathVariableMethodArgumentResolver<Dimension> {

  public static final String DIMENSION_MAPPING_NAME = "dimension";
  public static final String DIMENSION_URL_ELEMENT = "{" + DIMENSION_MAPPING_NAME + "}";

  private @Getter @Setter @Inject DatasetPathVariableMethodArgumentResolver datasetResolver;

  /**
   * 
   */
  public DimensionPathVariableMethodArgumentResolver () {
    super (Dimension.class, DIMENSION_MAPPING_NAME);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveObject(java.lang.String, org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public Dimension resolveObject (String value, Method method, NativeWebRequest request) throws Exception {
    return resolveDimension (datasetResolver.resolveObject (method, request), value);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveObject(java.lang.String,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public Dimension resolveObject (String value, NativeWebRequest request) throws Exception {
    return resolveDimension (datasetResolver.resolveObject (request), value);
  }

  private Dimension resolveDimension (Dataset dataset, String value) throws InvalidDimensionTypeException {
    return dataset.dimension (from (value));
  }
}
