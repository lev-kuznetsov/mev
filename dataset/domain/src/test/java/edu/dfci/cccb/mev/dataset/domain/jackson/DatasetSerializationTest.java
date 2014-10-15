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

package edu.dfci.cccb.mev.dataset.domain.jackson;

import static java.util.regex.Pattern.compile;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.supercsv.comment.CommentMatcher;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.CsvPreference.Builder;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.inject.Provides;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.annotation.NameOf;
import edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler;

@RunWith (JukitoRunner.class)
public class DatasetSerializationTest {

  public static class SerializationTestModule extends JukitoModule {
    @Provides
    @Singleton
    @NameOf (Dataset.class)
    public String dataset () {
      return "mock";
    }

    @Provides
    @Singleton
    public Dataset<String, Double> dataset (DatasetTsvMessageHandler handler) throws IOException {
      return handler.readFrom (null, null, null, null, null, new ByteArrayInputStream (("  \tc1\tc2\tc3\n" +
                                                                                        "r1\t.1\t.2\t.4\n" +
                                                                                        "r2\t.4\t.5\t.6").getBytes ()));
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

    @Provides
    @Singleton
    public ObjectMapper mapper () {
      ObjectMapper mapper = new ObjectMapper ();
      mapper.setAnnotationIntrospector (AnnotationIntrospector.pair (new JacksonAnnotationIntrospector (),
                                                                     new JaxbAnnotationIntrospector (mapper.getTypeFactory ())));
      return mapper;
    }

    protected void configureTest () {}
  }

  private @Inject ObjectMapper mapper;
  private @Inject Dataset<String, Double> dataset;

  @Test
  public void serialize () throws Exception {
    assertEquals ("{name:mock}", mapper.writeValueAsString (dataset), true);
  }
}
