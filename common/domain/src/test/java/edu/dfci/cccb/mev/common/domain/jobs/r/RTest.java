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

import static java.lang.Double.NaN;
import static java.lang.Double.*;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

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

import edu.dfci.cccb.mev.common.domain.guice.MevModule;
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
      bind (J2.class);
      install (new MevModule ());
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
    private final CountDownLatch c = new CountDownLatch (1);

    @Parameter
    private int param2 () {
      return 2;
    }

    @Callback
    private void callback (@Result Map<String, Double> result) {
      result2 = result;
      c.countDown ();
    }
  }

  @Test
  public void standardValues (Job job, R r) throws Exception {
    r.dispatch (job);
    job.c.await ();
    assertEquals (job.result.one, 4, .00001);
    assertEquals (job.result.two, 3, .00001);
    assertEquals (job.result2.get ("one"), 4, .00001);
    assertEquals (job.result2.get ("two"), 3, .00001);
  }

  @edu.dfci.cccb.mev.common.domain.jobs.r.annotation.R ("function (l) list (i=l[1],o=l[2],n=l[3])")
  public static class J2 {
    private final CountDownLatch c = new CountDownLatch (1);
    private @Parameter Double[] l = new Double[] { POSITIVE_INFINITY, null, NaN };
    private @Result Map<String, Double> r;

    @Callback
    private void cb () {
      c.countDown ();
    }
  }

  @Test
  public void specialValues (J2 j2, R r) throws Exception {
    r.dispatch (j2);
    j2.c.await ();
    assertTrue (isInfinite (j2.r.get ("i")));
    assertTrue (j2.r.get ("o") == null && j2.r.containsKey ("o"));
    assertTrue (isNaN (j2.r.get ("n")));
  }
}
