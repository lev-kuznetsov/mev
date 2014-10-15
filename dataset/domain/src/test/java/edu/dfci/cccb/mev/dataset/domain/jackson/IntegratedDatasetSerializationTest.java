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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.io.ByteArrayInputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provides;

import edu.dfci.cccb.mev.common.domain.guice.jackson.annotation.Handling;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.annotation.NameOf;
import edu.dfci.cccb.mev.dataset.domain.guice.DatasetModule;
import edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler;

@RunWith (JukitoRunner.class)
public class IntegratedDatasetSerializationTest {

  public static class IntegratedDatasetSerializationTestModule extends JukitoModule {
    @Provides
    @NameOf (Dataset.class)
    @Singleton
    public String dataset () {
      return "mock";
    }

    protected void configureTest () {
      install (new DatasetModule ());
    }
  }

  private @Inject @Handling (APPLICATION_JSON) ObjectMapper mapper;
  private @Inject DatasetTsvMessageHandler handler;

  @Test
  public void serialize () throws Exception {
    assertEquals ("{name:mock}",
                  mapper.writeValueAsString (handler.readFrom (null,
                                                               null,
                                                               null,
                                                               null,
                                                               null,
                                                               new ByteArrayInputStream (("  \tc1\tc2\tc3\n" +
                                                                                          "r1\t.1\t.2\t.4\n" +
                                                                                          "r2\t.4\t.5\t.6").getBytes ()))),
                  true);
  }
}
