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
package edu.dfci.cccb.mev.hcl.rest.resolvers;

import static edu.dfci.cccb.mev.hcl.domain.contract.Linkage.from;

import java.lang.reflect.Method;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.web.context.request.NativeWebRequest;

import edu.dfci.cccb.mev.dataset.rest.resolvers.AbstractTypedPathVariableMethodArgumentResolver;
import edu.dfci.cccb.mev.hcl.domain.contract.Linkage;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode (callSuper = true)
@ToString
public class LinkagePathVariableMethodArgumentResolver extends AbstractTypedPathVariableMethodArgumentResolver<Linkage> {

  public static final String LINKAGE_MAPPING_NAME = "linkage";
  public static final String LINKAGE_URL_ELEMENT = "{" + LINKAGE_MAPPING_NAME + "}";

  /**
   */
  public LinkagePathVariableMethodArgumentResolver () {
    super (Linkage.class, LINKAGE_MAPPING_NAME);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveObject(java.lang.String, org.springframework.core.MethodParameter,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public Linkage resolveObject (String value, Method method, NativeWebRequest request) throws Exception {
    return from (value);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.rest.resolvers.
   * AbstractTypedPathVariableMethodArgumentResolver
   * #resolveObject(java.lang.String,
   * org.springframework.web.context.request.NativeWebRequest) */
  @Override
  public Linkage resolveObject (String value, NativeWebRequest request) throws Exception {
    return from (value);
  }
}
