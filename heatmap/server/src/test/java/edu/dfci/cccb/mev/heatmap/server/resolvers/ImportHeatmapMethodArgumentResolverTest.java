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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.test.mock.MockHeatmap;
import edu.dfci.cccb.mev.test.mock.MockHeatmapBuilder;

/**
 * @author levk
 * 
 */
@SuppressWarnings ("deprecation")
public class ImportHeatmapMethodArgumentResolverTest {

  private ImportHeatmapMethodArgumentResolver resolver;
  private Heatmap heatmap;
  private MethodParameter applicable = parameter (0, MockMethodContainer.class, "mockImportHeatmap", Heatmap.class);
  private MethodParameter nonApplicable = parameter (0, MockMethodContainer.class, "mockString", String.class);
  private NativeWebRequest request = request ();

  {
    heatmap = new MockHeatmap ("mock");
    resolver = new ImportHeatmapMethodArgumentResolver (null, false, new MockHeatmapBuilder (heatmap));
  }

  @Test
  public void build () throws Exception {
    assertTrue (resolver.supportsParameter (applicable));
    assertEquals ("mock", ((Heatmap) resolver.resolveArgument (applicable, null, request, null)).name ());
  }

  @Test
  public void notSupported () {
    assertFalse (resolver.supportsParameter (nonApplicable));
  }

  private static MethodParameter parameter (int index, Class<?> clazz, String name, Class<?>... parameters) {
    try {
      return new MethodParameter (clazz.getMethod (name, parameters), index);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new AssertionError ();
    }
  }

  private static NativeWebRequest request () {
    MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest ();
    request.addFile (new MockMultipartFile ("matrix", new byte[0]));
    return new ServletWebRequest (request);
  }
}
