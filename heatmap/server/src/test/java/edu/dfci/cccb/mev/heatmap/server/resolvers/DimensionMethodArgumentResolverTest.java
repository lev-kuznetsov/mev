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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static edu.dfci.cccb.mev.heatmap.domain.Dimension.*;
import static org.junit.Assert.*;
import static org.springframework.web.servlet.HandlerMapping.*;

import edu.dfci.cccb.mev.heatmap.domain.Dimension;
import edu.dfci.cccb.mev.heatmap.domain.InvalidDimensionException;

/**
 * @author levk
 * 
 */
@SuppressWarnings ("deprecation")
public class DimensionMethodArgumentResolverTest {

  private DimensionMethodArgumentResolver resolver = new DimensionMethodArgumentResolver ();
  private MethodParameter applicable = parameter (0, MockMethodContainer.class, "mockDimension", Dimension.class);
  private MethodParameter nonApplicable = parameter (0, MockMethodContainer.class, "mockString", String.class);
  private MethodParameter misnamed = parameter (0, MockMethodContainer.class, "mockMisnamedDimension", Dimension.class);

  @Test
  public void applicableSupport () {
    assertTrue (resolver.supportsParameter (applicable));
  }

  @Test
  public void misnamedSupport () {
    assertTrue (resolver.supportsParameter (misnamed));
  }

  @Test
  public void notSupported () {
    assertFalse (resolver.supportsParameter (nonApplicable));
  }

  @Test
  public void row () throws Exception {
    assertEquals (ROW, resolver.resolveArgument (applicable, null, request ("row"), null));
  }

  @Test
  public void column () throws Exception {
    assertEquals (COLUMN, resolver.resolveArgument (applicable, null, request ("column"), null));
  }

  @Test
  public void x () throws Exception {
    assertEquals (COLUMN, resolver.resolveArgument (applicable, null, request ("x"), null));
  }

  @Test
  public void probe () throws Exception {
    assertEquals (ROW, resolver.resolveArgument (applicable, null, request ("probe"), null));
  }

  @Test
  public void gene () throws Exception {
    assertEquals (ROW, resolver.resolveArgument (applicable, null, request ("gene"), null));
  }

  @Test
  public void sample () throws Exception {
    assertEquals (COLUMN, resolver.resolveArgument (applicable, null, request ("sample"), null));
  }

  @Test (expected = InvalidDimensionException.class)
  public void junk () throws Exception {
    resolver.resolveArgument (applicable, null, request ("junk"), null);
    fail ();
  }

  @Test (expected = ServletRequestBindingException.class)
  public void misnamed () throws Exception {
    resolver.resolveArgument (misnamed, null, request ("sample"), null);
    fail ();
  }

  private static MethodParameter parameter (int index, Class<?> clazz, String name, Class<?>... parameters) {
    try {
      return new MethodParameter (clazz.getMethod (name, parameters), index);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new AssertionError ();
    }
  }

  private static NativeWebRequest request (String dimension) {
    Map<String, String> pathVariables = new HashMap<> ();
    pathVariables.put ("dimension", dimension);
    MockHttpServletRequest request = new MockHttpServletRequest ();
    request.setAttribute (URI_TEMPLATE_VARIABLES_ATTRIBUTE, pathVariables);
    return new ServletWebRequest (request);
  }
}
