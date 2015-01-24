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

package edu.dfci.cccb.mev.common.domain.jobs.r;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import edu.dfci.cccb.mev.common.domain.guice.rserve.annotation.Rserve;
import edu.dfci.cccb.mev.common.domain.jobs.Dispatcher;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Callback;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Result;

@RunWith (JukitoRunner.class)
@Category (Dispatcher.class)
public class RTest {

  public static class RModule extends JukitoModule {
    @Override
    protected void configureTest () {
      bind (Job.class);
      bind (InetSocketAddress.class).annotatedWith (Rserve.class)
                                    .toInstance (new InetSocketAddress ("localhost", 6311));
      ObjectMapper m = new ObjectMapper ();
      m.setAnnotationIntrospector (new JaxbAnnotationIntrospector (m.getTypeFactory ()));
      bind (ObjectMapper.class).toInstance (m);
      bind (Executor.class).annotatedWith (Rserve.class).toInstance (Executors.newSingleThreadExecutor ());
      bind (R.class);
    }
  }

  @XmlRootElement
  @XmlAccessorType (XmlAccessType.NONE)
  @ToString
  public static class Res {
    @XmlElement private double one;
    @XmlElement private double two;
  }

  @edu.dfci.cccb.mev.common.domain.jobs.r.annotation.R ("function (param) { list (one = param * 4, two = param + 2) }")
  @ToString
  public static class Job {
    @Parameter private int param = 1;
    @Result private Res result;
    private Map<String, Double> result2;

    @Parameter
    private int param2 () {
      return 2;
    }

    @Callback
    private void callback (@Result Map<String, Double> result) {
      result2 = result;
    }
  }

  @Test
  public void test (Job job, R r) {
    r.dispatch (job);
    assertEquals (job.result.one, 4, .00001);
    assertEquals (job.result.two, 3, .00001);
    assertEquals (job.result2.get ("one"), 4, .00001);
    assertEquals (job.result2.get ("two"), 3, .00001);
  }
}
