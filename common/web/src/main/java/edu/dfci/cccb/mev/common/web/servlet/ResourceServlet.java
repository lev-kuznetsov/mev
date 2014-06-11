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
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

/**
 * Resource serving servlet, in compliance with servlet 3 spec
 * <p/>
 * Despite my best efforts I couldn't get the standard Jetty default servlet to
 * behave that way in every circumstance (especially in embedded form during
 * unit testing serving resources from a jar)
 * 
 * @author levk
 * @since CRYSTAL
 */
@WebServlet (name = "resources", urlPatterns = "/*")
@Log4j
public class ResourceServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final String RESOURCE_ROOT = "/META-INF/resources";

  /* (non-Javadoc)
   * @see
   * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse) */
  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    URL url = toResourceUrl (toUri (request));
    if (log.isDebugEnabled ())
      log.debug ("Resolved resource request uri " + toUri (request) + " to " + url);
    if (url != null) {
      if (isDirectory (url))
        response.setStatus (SC_FORBIDDEN);
      else {
        URLConnection connection = url.openConnection ();
        response.setContentType (connection.getContentType ());
        response.setContentLength (connection.getContentLength ());
        response.setStatus (SC_OK);
        response.setContentType (new MimetypesFileTypeMap ().getContentType (url.getFile ()));

        // Disable caching
        response.setHeader ("Cache-Control", "no-cache, no-store");

        try (InputStream source = connection.getInputStream ();
             OutputStream target = response.getOutputStream ()) {
          copy (source, target);
        }
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
  protected URL toResourceUrl (String uri) {
    return getClass ().getResource (RESOURCE_ROOT + uri);
  }

  /**
   * @param request
   * @return request URI
   */
  protected String toUri (HttpServletRequest request) {
    String uri = request.getRequestURI ();
    if ("/".equals (uri))
      uri = "/index.html";
    return uri;
  }

  /**
   * @param url
   * @return true if the supplied URL is a directory (actual file system or
   *         inside jar)
   * @throws IOException
   */
  private boolean isDirectory (URL url) throws IOException {
    String protocol = url.getProtocol ();
    if (protocol.equals ("file")) {
      return new File (url.getFile ()).isDirectory ();
    }
    if (protocol.equals ("jar")) {
      String file = url.getFile ();
      int bangIndex = file.indexOf ('!');
      String jarPath = file.substring (bangIndex + 2);
      file = new URL (file.substring (0, bangIndex)).getFile ();
      try (ZipFile zip = new ZipFile (file)) {
        ZipEntry entry = zip.getEntry (jarPath);
        boolean isDirectory = entry.isDirectory ();
        if (!isDirectory) {
          InputStream input = zip.getInputStream (entry);
          isDirectory = input == null;
          if (input != null)
            input.close ();
        }
        return isDirectory;
      }
    }
    return false;
  }
}
