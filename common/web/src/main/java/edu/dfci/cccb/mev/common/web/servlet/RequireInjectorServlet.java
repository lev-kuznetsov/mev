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

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
import static java.util.Arrays.asList;
import static java.util.Collections.enumeration;
import static java.util.Collections.list;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author levk
 * @since CRYSTAL
 */
@WebServlet (name = "injector", urlPatterns = "/require-configuration")
@Log4j
public class RequireInjectorServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final String CONFIGURATION_RESOURCE = "META-INF/configuration/require.json";

  private static final JsonNode CONFIGURATION;

  private static ObjectNode merge (JsonNodeFactory factory, ObjectNode... nodes) {
    ObjectNode result = factory.objectNode ();
    for (ObjectNode node : nodes)
      for (Iterator<Entry<String, JsonNode>> fields = node.fields (); fields.hasNext ();) {
        Entry<String, JsonNode> field = fields.next ();
        JsonNode current = result.get (field.getKey ());
        if (current == null)
          result.put (field.getKey (), field.getValue ());
        else
          try {
            if (current instanceof ObjectNode)
              result.put (field.getKey (), merge (factory, (ObjectNode) current, (ObjectNode) field.getValue ()));
            else if (current instanceof ArrayNode)
              result.put (field.getKey (), merge (factory, (ArrayNode) current, (ArrayNode) field.getValue ()));
            else {
              log.fatal ("Unable to merge nodes of type " + current.getClass ().getSimpleName ());
            }
          } catch (ClassCastException e) {
            log.fatal ("Unexpected type for field " + field.getKey () + " while configuring require");
            throw new RuntimeException (e);
          }
      }
    return result;
  }

  private static ArrayNode merge (JsonNodeFactory factory, ArrayNode... nodes) {
    ArrayNode result = factory.arrayNode ();
    for (ArrayNode node : nodes)
      for (JsonNode element : node)
        result.add (element);
    return result;
  }

  private static InputStream bracket (InputStream input) {
    return new SequenceInputStream (enumeration (asList (new ByteArrayInputStream ("{".getBytes ()),
                                                         input,
                                                         new ByteArrayInputStream ("}".getBytes ()))));
  }

  static {
    try {
      List<URL> configurations = list (RequireInjectorServlet.class.getClassLoader ()
                                                                   .getResources (CONFIGURATION_RESOURCE));
      log.debug ("Found configurations " + configurations);
      ObjectMapper mapper = new ObjectMapper () {
        private static final long serialVersionUID = 1L;

        {
          configure (ALLOW_UNQUOTED_FIELD_NAMES, true);
          configure (ALLOW_SINGLE_QUOTES, true);
          configure (ALLOW_COMMENTS, true);
        }
      };
      ObjectNode configuration = new ObjectNode (mapper.getNodeFactory ());
      for (Iterator<URL> resources = configurations.iterator (); resources.hasNext ();)
        configuration = merge (mapper.getNodeFactory (),
                               configuration,
                               mapper.readValue (bracket (resources.next ().openStream ()), ObjectNode.class));
      CONFIGURATION = configuration;
      log.info ("Full require configuration: " + CONFIGURATION);
    } catch (IOException e) {
      log.fatal ("Unable to configure require", e);
      throw new Error (e);
    }
  }

  public static JsonNode configuration () throws IOException {
    return CONFIGURATION;
  }

  private static String define () throws IOException {
    return "define ([], function () { return "
           + new ObjectMapper ().writeValueAsString (configuration ()) + "});";
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
      out.println (define ());
    }
  }
}
