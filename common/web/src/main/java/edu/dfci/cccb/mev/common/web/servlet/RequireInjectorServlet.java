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

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static java.lang.ClassLoader.getSystemResources;
import static java.util.Collections.enumeration;
import static java.util.Collections.list;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author levk
 * @since CRYSTAL
 */
@WebServlet (name = "injector", urlPatterns = "/require")
public class RequireInjectorServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public static final String CONFIGURATION = "META-INF/configuration/require.json";

  private static InputStream source (final String path) throws IOException {
    return new InputStream () {
      private final InputStream configuration;

      {
        List<InputStream> sequence = new ArrayList<> ();
        sequence.add (new ByteArrayInputStream ("{".getBytes ()));
        for (Iterator<URL> resources = list (getSystemResources (path)).iterator (); resources.hasNext ();) {
          sequence.add (resources.next ().openStream ());
          if (resources.hasNext ())
            sequence.add (new ByteArrayInputStream (",".getBytes ()));
        }
        sequence.add (new ByteArrayInputStream ("}".getBytes ()));
        configuration = new SequenceInputStream (enumeration (sequence));
      }

      @Override
      public int read () throws IOException {
        return configuration.read ();
      }
    };
  }

  public static JsonNode configuration () throws IOException {
    return new ObjectMapper () {
      private static final long serialVersionUID = 1L;

      {
        configure (ALLOW_UNQUOTED_FIELD_NAMES, true);
        configure (ALLOW_SINGLE_QUOTES, true);
      }
    }.readValue (source (CONFIGURATION), JsonNode.class);
  }

  private static String define (String variable) throws IOException {
    return "var " + variable + " = "
           + new ObjectMapper () {
             private static final long serialVersionUID = 1L;

             {
               enable (INDENT_OUTPUT);
             }
           }.writeValueAsString (configuration ()) + ";";
  }

  /* (non-Javadoc)
   * @see
   * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse) */
  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setStatus (SC_OK);
    response.setContentType ("application/x-javascript");
    try (PrintStream out = new PrintStream (response.getOutputStream ())) {
      out.println (define ("require"));
    }
  }
}
