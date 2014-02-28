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

import static com.google.common.io.ByteStreams.copy;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Resource serving servlet, in compliance with servlet 3 spec
 * <p/>
 * Despite my best efforts I couldn't get the standard Jetty default servlet to
 * behave that way in every circumstance (especially in embedded form during
 * unit testing)
 * 
 * @author levk
 * @since CRYSTAL
 */
@WebServlet (name = "resources", urlPatterns = "/*")
public class ResourceServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see
   * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse) */
  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    URL url = toResourceUrl (request);
    if (url != null) {
      URLConnection connection = url.openConnection ();
      response.setContentType (connection.getContentType ());
      response.setContentLength (connection.getContentLength ());
      response.setStatus (SC_OK);
      try (InputStream source = connection.getInputStream ();
           OutputStream target = response.getOutputStream ()) {
        copy (source, target);
      }
    } else
      response.setStatus (SC_NOT_FOUND);
  }

  /**
   * @param request incoming
   * @return URL to valid resource or null if not found
   * @throws ServletException
   * @throws IOException
   */
  protected URL toResourceUrl (HttpServletRequest request) throws ServletException, IOException {
    return getClass ().getResource ("/META-INF/resources" + request.getRequestURI ());
  }
}
