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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Jetty9 wrapper, relies on servlet 3 XML-less configuration via annotations
 * 
 * @author levk
 */
public class Jetty9 implements AutoCloseable {

  private Server server;

  public Jetty9 () throws Exception {
    WebAppContext context = new WebAppContext ();
    context.setConfigurations (new Configuration[] { new Servlet3Jetty9AnnotationConfiguration () });
    server = new Server (0);
    server.setHandler (context);
    server.start ();
  }

  public int port () {
    return ((ServerConnector) server.getConnectors ()[0]).getLocalPort ();
  }

  /* (non-Javadoc)
   * @see java.lang.AutoCloseable#close() */
  @Override
  public void close () throws Exception {
    server.stop ();
    server.join ();
  }
}
