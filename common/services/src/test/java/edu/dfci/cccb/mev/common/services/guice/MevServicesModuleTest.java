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

package edu.dfci.cccb.mev.common.services.guice;

import static org.apache.cxf.helpers.IOUtils.readStringFromStream;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

import edu.dfci.cccb.mev.common.test.jetty.Jetty9;

/**
 * @author levk
 */
public class MevServicesModuleTest {

  @Test
  public void discoveredTestModule () throws Exception {
    try (Jetty9 jetty = new Jetty9 ()) {
      URL url = new URL ("http://localhost:" + jetty.port () + "/services/pojo?word=hello&number=5");
      assertEquals ("(hello,5)",
                    readStringFromStream (url.openStream ()));
    }
  }
}
