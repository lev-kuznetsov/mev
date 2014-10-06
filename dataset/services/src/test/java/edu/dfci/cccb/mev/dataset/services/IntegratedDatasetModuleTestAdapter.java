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

package edu.dfci.cccb.mev.dataset.services;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

import edu.dfci.cccb.mev.common.test.jetty.Jetty9;
import edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler;

public abstract class IntegratedDatasetModuleTestAdapter {

  private final String root;

  protected IntegratedDatasetModuleTestAdapter (String root) {
    this.root = root;
  }

  protected IntegratedDatasetModuleTestAdapter () {
    this ("/test");
  }

  protected String list (Jetty9 j) throws Exception {
    HttpURLConnection c = j.connect (root + "/dataset");
    c.setRequestMethod ("GET");
    c.setRequestProperty ("Accept", MediaType.APPLICATION_JSON);
    ByteArrayOutputStream buffer = new ByteArrayOutputStream ();
    IOUtils.copy (c.getInputStream (), buffer);
    return buffer.toString ();
  }

  protected void put (Jetty9 j, String name, String tsv) throws Exception {
    HttpURLConnection c = j.connect (root + "/dataset/" + name);
    c.setRequestProperty ("Accept", MediaType.APPLICATION_JSON);
    c.setRequestMethod ("PUT");
    c.setRequestProperty ("Content-Type", DatasetTsvMessageHandler.TEXT_TSV);
    c.setDoInput (true);
    c.setDoOutput (true);
    IOUtils.copy (new ByteArrayInputStream (tsv.getBytes ()), c.getOutputStream ());
    assertThat (c.getResponseCode (), is (204));
  }

  protected String get (Jetty9 j, String name) throws Exception {
    HttpURLConnection c = j.connect (root + "/dataset/" + name);
    c.setRequestMethod ("GET");
    c.setRequestProperty ("Accept", MediaType.APPLICATION_JSON);
    ByteArrayOutputStream buffer = new ByteArrayOutputStream ();
    IOUtils.copy (c.getInputStream (), buffer);
    return buffer.toString ();
  }
}
