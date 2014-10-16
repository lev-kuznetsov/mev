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

import static edu.dfci.cccb.mev.dataset.domain.Dataset.DATASET;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.ROW;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.supercsv.comment.CommentMatcher;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.CsvPreference.Builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.SessionScoped;

import edu.dfci.cccb.mev.common.domain.guice.jackson.annotation.Handling;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.MessageReaderBinder;
import edu.dfci.cccb.mev.common.domain.guice.jaxrs.MessageWriterBinder;
import edu.dfci.cccb.mev.common.domain.messages.JacksonMessageHandler;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ContentNegotiationConfigurer;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceModule;
import edu.dfci.cccb.mev.common.test.jetty.Jetty9;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.annotation.NameOf;
import edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;

public class DatasetControllerTest {

  @Test
  public void integratedPutThenGet () throws Exception {
    try (Jetty9 j = new Jetty9 ()) {
      test ("/services", j);
    }
  }

  @Test
  public void putThenGet () throws Exception {
    try (Jetty9 j = new Jetty9 (new ServiceModule () {
      @Provides
      @Named (ROW)
      @Singleton
      public String row () {
        return "row";
      }

      @Provides
      @Named (COLUMN)
      @Singleton
      public String column () {
        return "column";
      }

      @Provides
      @NameOf (Dataset.class)
      @RequestScoped
      public String dataset (UriInfo uri) {
        return uri.getPathParameters ().getFirst (DATASET);
      }

      @Provides
      @Singleton
      public CsvPreference preference () {
        return new Builder ('"', '\t', "\r\n").skipComments (new CommentMatcher () {
          private final Pattern[] comments = { compile ("^#[^$]$") };

          public boolean isComment (String line) {
            for (Pattern comment : comments)
              if (comment.matcher (line).matches ())
                return true;
            return false;
          }
        }).build ();
      }

      public void configure (MessageReaderBinder binder) {
        binder.use (DatasetTsvMessageHandler.class);
      }

      public void configure (MessageWriterBinder binder) {
        binder.use (JacksonMessageHandler.class);
      }

      public void configure (ResourceBinder binder) {
        binder.publish ("dataset", new TypeLiteral<Map<String, Dataset<String, Double>>> () {})
              .toProvider (new Provider<Map<String, Dataset<String, Double>>> () {
                public Map<String, Dataset<String, Double>> get () {
                  return DatasetAdapter.workspace ();
                }
              }).in (SessionScoped.class);
      }

      public void configure (ContentNegotiationConfigurer content) {
        content.parameter ("format").map ("tsv", DatasetTsvMessageHandler.TEXT_TSV_TYPE);
      }

      public void configure (ServiceBinder binder) {
        binder.service ("/test/*");
      }

      @Provides
      @Singleton
      @Handling (MediaType.APPLICATION_JSON)
      public ObjectMapper mapper () {
        ObjectMapper mapper = new ObjectMapper ();
        mapper.setAnnotationIntrospector (new JaxbAnnotationIntrospector (mapper.getTypeFactory ()));
        return mapper;
      }
    })) {
      test ("/test", j);
    }
  }

  private void test (String svc, Jetty9 j) throws Exception {
    HttpURLConnection c = j.connect (svc + "/dataset/hello");
    c.setRequestProperty ("Accept", MediaType.APPLICATION_JSON);
    c.setRequestMethod ("PUT");
    c.setRequestProperty ("Content-Type", DatasetTsvMessageHandler.TEXT_TSV);
    c.setDoInput (true);
    c.setDoOutput (true);
    IOUtils.copy (new ByteArrayInputStream (("\tc1\tc2\n" +
                                             "r1\t.1\t.2\n" +
                                             "r2\t.3\t.4").getBytes ()), c.getOutputStream ());
    assertThat (c.getResponseCode (), is (204));

    c = j.connect (svc + "/dataset/hello2");
    c.setRequestProperty ("Accept", MediaType.APPLICATION_JSON);
    c.setRequestMethod ("PUT");
    c.setRequestProperty ("Content-Type", DatasetTsvMessageHandler.TEXT_TSV);
    c.setDoInput (true);
    c.setDoOutput (true);
    IOUtils.copy (new ByteArrayInputStream (("\tc1\tc2\n" +
                                             "r1\t.1\t.2\n" +
                                             "r2\t.3\t.4").getBytes ()), c.getOutputStream ());
    assertThat (c.getResponseCode (), is (204));

    c = j.connect (svc + "/dataset");
    c.setRequestMethod ("GET");
    c.setRequestProperty ("Accept", MediaType.APPLICATION_JSON);
    ByteArrayOutputStream buffer = new ByteArrayOutputStream ();
    IOUtils.copy (c.getInputStream (), buffer);
    assertEquals ("[{name:hello2},{name:hello}]", buffer.toString (), true);

    c = j.connect (svc + "/dataset/hello");
    c.setRequestMethod ("GET");
    c.setRequestProperty ("Accept", MediaType.APPLICATION_JSON);
    buffer = new ByteArrayOutputStream ();
    IOUtils.copy (c.getInputStream (), buffer);
    assertEquals ("{name:hello}", buffer.toString (), true);
  }
}
