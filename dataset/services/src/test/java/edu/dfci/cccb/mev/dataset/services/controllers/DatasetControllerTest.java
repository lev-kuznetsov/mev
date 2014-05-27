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

package edu.dfci.cccb.mev.dataset.services.controllers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import edu.dfci.cccb.mev.common.test.jetty.Jetty9;

public class DatasetControllerTest {

  @Test
  public void get () throws Exception {
    try (Jetty9 jetty = new Jetty9 ()) {
      HttpURLConnection connection = (HttpURLConnection) new URL ("http://localhost:"
                                                                  + jetty.port ()
                                                                  + "/services/dataset/hello").openConnection ();
      connection.setRequestMethod ("PUT");
      connection.setRequestProperty ("Content-Type", MediaType.TEXT_PLAIN);
      connection.setDoInput (true);
      connection.setDoOutput (true);
      try (PrintStream data = new PrintStream (connection.getOutputStream ())) {
        data.print ("\tc1\tc2\n" +
                    "r1\t.1\t.2\n" +
                    "r2\t.3\t.4");
        assertThat (connection.getResponseCode (), is (204));

        assertEquals ("{name:hello,"
                      + "dimensions:[{name:row,keys:[r1,r2]},{name:column,keys:[c1,c2]}],"
                      + "values:[{coordinates:{column:c1,row:r1},value:0.1},"
                      + "{coordinates:{column:c2,row:r1},value:0.2},"
                      + "{coordinates:{column:c1,row:r2},value:0.3},"
                      + "{coordinates:{column:c2,row:r2},value:0.4}],"
                      + "analyses:[]}",
                      jetty.get ("/services/dataset/hello?format=json"),
                      false);
      }
    }
  }
}
