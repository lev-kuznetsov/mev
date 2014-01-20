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

import static edu.dfci.cccb.mev.dataset.domain.contract.Selection.VALID_SELECTION_NAME_REGEX;

import java.lang.reflect.Method;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.web.context.request.NativeWebRequest;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;

/**
 * @author levk
 * 
 */
@ToString
public class SelectionPathVariableMethodArgumentResolver extends AbstractTypedPathVariableMethodArgumentResolver<Selection> {

  public static final String SELECTION_MAPPING_NAME = "selection";
  public static final String SELECTION_URL_ELEMENT = "{" + SELECTION_MAPPING_NAME + ":"
                                                     + VALID_SELECTION_NAME_REGEX + "}";

  private @Getter @Setter @Inject DimensionPathVariableMethodArgumentResolver dimensionResolver;

  /**
   * 
   */
  public SelectionPathVariableMethodArgumentResolver () {
    super (Selection.class, SELECTION_MAPPING_NAME);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveObject(java.lang.String, org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public Selection resolveObject (String value, Method method, NativeWebRequest request) throws Exception {
    return resolveSelection (dimensionResolver.resolveObject (method, request), value);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveObject(java.lang.String,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public Selection resolveObject (String value, NativeWebRequest request) throws Exception {
    return resolveSelection (dimensionResolver.resolveObject (request), value);
  }

  private Selection resolveSelection (Dimension dimension, String name) throws SelectionNotFoundException {
    return dimension.selections ().get (name);
  }
}
