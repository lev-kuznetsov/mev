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
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

import us.levk.spring.web.mock.MockNativeWebRequest;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.mock.MockHeatmap.MockBuilder;

/**
 * @author levk
 * 
 */
public class RequestBodyHeatmapMethodArgumentResolverTest {

  private RequestBodyHeatmapMethodArgumentResolver resolver;
  private Heatmap heatmap;

  {
    heatmap = heatmap ("hello");
    resolver = new RequestBodyHeatmapMethodArgumentResolver (new MockBuilder ().content (heatmap));
  }

  @Test
  public void test () throws Exception {
    MethodParameter p = parameter (1);
    assertEquals (resolver.supportsParameter (p), true);
    assertEquals (((Heatmap) resolver.resolveArgument (p,
                                                       null,
                                                       request (new ByteArrayInputStream ("hello".getBytes ())),
                                                       null)).name (),
                  "hello");
  }

  public static class MockClass {
    @Deprecated
    public void mockMethod (String junk, @RequestParam ("input") Heatmap heatmap) {}
  }

  private static MethodParameter parameter (int index) throws NoSuchMethodException, SecurityException {
    return new MethodParameter (MockClass.class.getMethod ("mockMethod", String.class, Heatmap.class), index);
  }

  private static MockNativeWebRequest request (InputStream file) throws IOException {
    MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest ();
    request.addFile (new MockMultipartFile ("input", file));
    return new MockNativeWebRequest ().setNativeRequest (request);
  }
}
