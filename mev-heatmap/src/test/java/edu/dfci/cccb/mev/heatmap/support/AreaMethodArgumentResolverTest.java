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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import us.levk.spring.web.mock.MockNativeWebRequest;
import edu.dfci.cccb.mev.heatmap.domain.Area;
import edu.dfci.cccb.mev.heatmap.domain.InvalidDataRequestException;

/**
 * @author levk
 * 
 */
public class AreaMethodArgumentResolverTest {
  
  private AreaMethodArgumentResolver resolver = new AreaMethodArgumentResolver ();

  @Test
  public void pattern1 () {
    assertTrue (Pattern.compile (AreaMethodArgumentResolver.AREA_STRING_REPRESENTATION_PATTERN_EXPRESSION).matcher ("[0:9,3:12]").matches ());
  }

  @Test
  public void pattern2 () {
    assertTrue (Pattern.compile (AreaMethodArgumentResolver.AREA_STRING_REPRESENTATION_PATTERN_EXPRESSION)
                       .matcher ("[324:9234,32423:12234]")
                       .matches ());
  }

  @Test
  public void simple () throws Exception {
    MethodParameter p = parameter (1);
    NativeWebRequest r = request ("[0:9,3:12]");
    assertTrue (resolver.supportsParameter (p));
    Area resolved = (Area) resolver.resolveArgument (p, null, r, null);
    assertNotNull (resolved);
    assertEquals (9, resolved.intervals ().get (0).getValue1 ().end ());
  }
  
  @Test (expected = InvalidDataRequestException.class)
  public void junk () throws Exception {
    MethodParameter p = parameter (1);
    NativeWebRequest r = request ("junk");
    assertTrue (resolver.supportsParameter (p));
    resolver.resolveArgument (p, null, r, null);
    fail ();
  }

  public static class MockMethodClass {
    @Deprecated
    public void mockMethod (String junk, @PathVariable ("area") Area area) {}
  }

  private static MethodParameter parameter (int index) throws NoSuchMethodException, SecurityException {
    return new MethodParameter (MockMethodClass.class.getMethod ("mockMethod", String.class, Area.class), index);
  }
  
  private static NativeWebRequest request (final String attribute) {
    return new MockNativeWebRequest () {
      {
        setAttribute (HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, new HashMap<String, String> () {
          private static final long serialVersionUID = 1L;

          {
            put ("area", attribute);
          }
        }, RequestAttributes.SCOPE_REQUEST);
      }
    };
  }
}
