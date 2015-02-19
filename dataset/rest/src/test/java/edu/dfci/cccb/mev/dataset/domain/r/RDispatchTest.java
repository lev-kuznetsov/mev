/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.domain.r;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import lombok.Getter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = RDispatcherConfiguration.class)
public class RDispatchTest {
  private @Autowired RDispatcher r;

  @R ("function (param) list (p = param)")
  public static class J {
    private @Parameter String param = "hello";

    public static class R {
      private @Getter @JsonProperty String p;
    }

    private @Getter @Result R r;
  }

  @Test
  @Ignore
  // This test will only work if there's an R backend active
  public void simple () {
    J j = new J ();
    r.execute (j);
    assertThat (j.getR ().getP (), is ("hello"));
  }
}
