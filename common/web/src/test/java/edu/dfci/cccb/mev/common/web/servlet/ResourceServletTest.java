/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.common.web.servlet;

import static org.apache.log4j.lf5.util.StreamUtils.getBytes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import edu.dfci.cccb.mev.common.test.jetty.Jetty9;

public class ResourceServletTest {

  @Test
  public void local () throws Exception {
    try (Jetty9 jetty = new Jetty9 ()) {
      assertEquals ("hello world",
                    new String (getBytes (new URL ("http://localhost:"
                                                   + jetty.port () + "/test.txt").openConnection ()
                                                                                 .getInputStream ())));
    }
  }

  @Test
  public void jar () throws Exception {
    try (Jetty9 jetty = new Jetty9 ()) {
      URL url = new URL ("http://localhost:"
                         + jetty.port ()
                         + "/webjars/jquery/2.1.0/webjars-requirejs.js");
      assertTrue (new String (getBytes (url.openConnection ()
                                           .getInputStream ())).startsWith ("/*global requirejs */"));
    }
  }

  @Test
  public void localInFolder () throws Exception {
    try (Jetty9 jetty = new Jetty9 ()) {
      assertEquals ("hello world",
                    new String (getBytes (new URL ("http://localhost:"
                                                   + jetty.port () + "/fold.er/test2.txt").openConnection ()
                                                                                          .getInputStream ())));
    }
  }

  @Test
  public void notFound () throws Exception {
    try (Jetty9 jetty = new Jetty9 ()) {
      assertEquals (404, jetty.rc ("/not_found"));
    }
  }

  @Test
  public void contentType () throws Exception {
    try (Jetty9 jetty = new Jetty9 ()) {
      assertEquals (new URL ("http://localhost:"
                             + jetty.port ()
                             + "/webjars/jquery/2.1.0/webjars-requirejs.js").openConnection ().getContentType (),
                    "application/javascript");
    }
  }
}
