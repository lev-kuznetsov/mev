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
import static org.junit.Assert.fail;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapNotFoundException;
import edu.dfci.cccb.mev.heatmap.domain.Workspace;
import edu.dfci.cccb.mev.heatmap.domain.concrete.ListWorkspace;
import edu.dfci.cccb.mev.test.mock.MockHeatmap;

/**
 * @author levk
 * 
 */
@SuppressWarnings ("deprecation")
public class WorkspaceHeatmapMethodArgumentResolverTest {

  private MethodParameter applicable = parameter (0, MockMethodContainer.class, "mockPathHeatmap", Heatmap.class);
  private MethodParameter nonApplicable = parameter (0, MockMethodContainer.class, "mockString", String.class);
  private WorkspaceHeatmapMethodArgumentResolver resolver;
  private Heatmap one;
  private Heatmap two;
  private Workspace workspace;

  {
    one = new MockHeatmap ("one");
    two = new MockHeatmap ("two");
    workspace = new ListWorkspace ();
    workspace.put (one);
    workspace.put (two);
    resolver = new WorkspaceHeatmapMethodArgumentResolver (workspace);
  }
  
  @Test
  public void support () {
    assertTrue (resolver.supportsParameter (applicable));
  }

  @Test
  public void one () throws Exception {
    assertEquals (one, resolver.resolveArgument (applicable, null, request ("one"), null));
  }

  @Test
  public void two () throws Exception {
    assertEquals (two, resolver.resolveArgument (applicable, null, request ("two"), null));
  }

  @Test (expected = HeatmapNotFoundException.class)
  public void junk () throws Exception {
    resolver.resolveArgument (applicable, null, request ("junk"), null);
    fail ();
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

  private static NativeWebRequest request (String heatmap) {
    Map<String, String> pathVariables = new HashMap<> ();
    pathVariables.put ("heatmap", heatmap);
    MockHttpServletRequest request = new MockHttpServletRequest ();
    request.setAttribute (URI_TEMPLATE_VARIABLES_ATTRIBUTE, pathVariables);
    return new ServletWebRequest (request);
  }
}
