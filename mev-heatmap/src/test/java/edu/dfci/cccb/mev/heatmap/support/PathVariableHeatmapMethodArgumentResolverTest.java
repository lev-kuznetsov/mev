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

import static edu.dfci.cccb.mev.heatmap.domain.mock.MockHeatmap.heatmap;
import static edu.dfci.cccb.mev.heatmap.domain.mock.MockWorkspace.workspace;
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
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapNotFoundException;

/**
 * @author levk
 * 
 */
public class PathVariableHeatmapMethodArgumentResolverTest {

  private final Heatmap one;
  private final Heatmap two;
  private final PathVariableHeatmapMethodArgumentResolver resolver;

  {
    one = heatmap ("one");
    two = heatmap ("two");
    resolver = new PathVariableHeatmapMethodArgumentResolver (workspace (one, two));
  }

  @Test
  public void one () throws Exception {
    MethodParameter p = parameter (1);
    ModelAndViewContainer mav = mav ();
    NativeWebRequest r = request ("one");
    assertEquals (resolver.supportsParameter (p), true);
    assertEquals (resolver.resolveArgument (p, mav, r, null), one);
  }

  @Test
  public void two () throws Exception {
    MethodParameter p = parameter (1);
    ModelAndViewContainer mav = mav ();
    NativeWebRequest r = request ("two");
    assertEquals (resolver.supportsParameter (p), true);
    assertEquals (resolver.resolveArgument (p, mav, r, null), two);
  }

  @Test
  public void string () throws Exception {
    MethodParameter p = parameter (0);
    assertEquals (resolver.supportsParameter (p), false);
  }

  @Test (expected = HeatmapNotFoundException.class)
  public void junk () throws Exception {
    MethodParameter p = parameter (1);
    ModelAndViewContainer mav = mav ();
    NativeWebRequest r = request ("junk");
    assertEquals (resolver.supportsParameter (p), true);
    resolver.resolveArgument (p, mav, r, null);
    fail ();
  }

  private static MethodParameter parameter (int index) throws NoSuchMethodException {
    return new MethodParameter (MockMethodClass.class.getMethod ("mockMethod",
                                                                 String.class,
                                                                 Heatmap.class), index);
  }

  public static class MockMethodClass {
    @Deprecated
    public void mockMethod (String foo, @PathVariable ("id") Heatmap heatmap) {}
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
            put ("id", attribute);
          }
        }, RequestAttributes.SCOPE_REQUEST);
      }
    };
  }
}
