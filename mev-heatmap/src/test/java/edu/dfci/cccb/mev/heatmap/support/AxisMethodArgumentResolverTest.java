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

import static edu.dfci.cccb.mev.heatmap.domain.Axis.COLUMN;
import static edu.dfci.cccb.mev.heatmap.domain.Axis.ROW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import us.levk.spring.web.mock.MockNativeWebRequest;
import edu.dfci.cccb.mev.heatmap.domain.Axis;
import edu.dfci.cccb.mev.heatmap.domain.InvalidDimensionException;

/**
 * @author levk
 * 
 */
public class AxisMethodArgumentResolverTest {

  private AxisMethodArgumentResolver resolver = new AxisMethodArgumentResolver ();

  @Test
  public void row () throws Exception {
    MethodParameter p = parameter (1);
    assertEquals (resolver.supportsParameter (p), true);
    assertEquals (resolver.resolveArgument (parameter (1), mav (), request ("row"), null), ROW);
  }

  @Test
  public void column () throws Exception {
    MethodParameter p = parameter (1);
    assertEquals (resolver.supportsParameter (p), true);
    assertEquals (resolver.resolveArgument (p, mav (), request ("column"), null), COLUMN);
  }

  @Test
  public void x () throws Exception {
    MethodParameter p = parameter (1);
    assertEquals (resolver.supportsParameter (p), true);
    assertEquals (resolver.resolveArgument (p, mav (), request ("x"), null), COLUMN);
  }

  @Test
  public void string () throws Exception {
    MethodParameter p = parameter (0);
    assertEquals (resolver.supportsParameter (p), false);
  }

  @Test (expected = InvalidDimensionException.class)
  public void junk () throws Exception {
    MethodParameter p = parameter (1);
    assertEquals (resolver.supportsParameter (p), true);
    resolver.resolveArgument (p, mav (), request ("junk"), null);
    fail ();
  }

  public static class MockMethodClass {
    @Deprecated
    public void mockMethod (String junk, @PathVariable ("dimension") Axis dimension) {}
  }

  private static MethodParameter parameter (int index) throws NoSuchMethodException {
    return new MethodParameter (MockMethodClass.class.getMethod ("mockMethod",
                                                                 String.class,
                                                                 Axis.class), index);
  }

  private static ModelAndViewContainer mav () {
    return new ModelAndViewContainer ();
  }

  private static NativeWebRequest request (final String attribute) {
    return new MockNativeWebRequest () {
      {
        setAttribute (HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, new HashMap<String, String> () {
          private static final long serialVersionUID = 1L;

          {
            put ("dimension", attribute);
          }
        }, RequestAttributes.SCOPE_REQUEST);
      }
    };
  }
}
