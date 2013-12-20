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
package edu.dfci.cccb.mev.web;

import java.net.HttpURLConnection;
import java.net.URL;

import lombok.extern.log4j.Log4j;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author levk
 * 
 */
@Log4j
public class RestTest {

  private static final int PORT = 9090; // Over 9000
  private static Server server;

  // @BeforeClass
  public static void startMev () throws Exception {
    log.info ("Starting embedded jetty for unit tests");
    WebAppContext context = new WebAppContext ();
    context.setContextPath ("/");
    context.setResourceBase ("src/main/webapp");
    context.setConfigurations (new Configuration[] { new JavaConfigAnnotationConfiguration () });
    server = new Server (PORT);
    server.setHandler (context);
    server.start ();
    log.info ("Embedded jetty for unit tests started");
  }

  // @Test
  public void test () throws Exception {
    HttpURLConnection c = (HttpURLConnection) new URL ("http://localhost:" + PORT + "/api").openConnection ();
    c.setRequestMethod ("GET");
    System.out.println ("RC=" + c.getResponseCode ());
    IOUtils.copy (c.getInputStream (), System.out);
  }

  // @AfterClass
  public static void stopMev () throws Exception {
    log.info ("Stopping jetty");
    server.stop ();
    server.join ();
    log.info ("Jetty stopped");
  }
}
