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

package edu.dfci.cccb.mev.common.test.jetty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class Jetty9Test {

  @Test
  public synchronized void listener () throws Exception {
    assertFalse (TestListener.isActive ());
    try (Jetty9 jetty = new Jetty9 ()) {
      assertTrue (TestListener.isActive ());
    }
    assertFalse (TestListener.isActive ());
  }

  @Test
  public synchronized void servlet () throws Exception {
    try (Jetty9 jetty = new Jetty9 ()) {
      assertEquals (555,
                    ((HttpURLConnection) new URL ("http://localhost:" + jetty.port () + "/test/servlet").openConnection ()).getResponseCode ());
    }
  }

  @Test
  public synchronized void filter () throws Exception {
    try (Jetty9 jetty = new Jetty9 ()) {
      assertEquals (888,
                    ((HttpURLConnection) new URL ("http://localhost:" + jetty.port () + "/test/filter").openConnection ()).getResponseCode ());
    }
  }
}
