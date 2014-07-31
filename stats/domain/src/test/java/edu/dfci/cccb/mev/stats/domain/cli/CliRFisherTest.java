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
package edu.dfci.cccb.mev.stats.domain.cli;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import javax.script.ScriptEngineManager;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import edu.dfci.cccb.mev.stats.domain.contract.Fisher;

/**
 * @author levk
 * 
 */
public class CliRFisherTest {

  @Test
  public void test () throws Exception {
    Fisher f = new CliRFisherBuilder ().setR (new ScriptEngineManager ().getEngineByName ("CliR"))
    											.name("test")
                                                 .m (4)
                                                 .n (5)
                                                 .s (6)
                                                 .t (7)
                                                 .build ();
    assertThat (f.oddsRatio (), closeTo (0.9362, 0.001));
    assertThat (f.pValue (), closeTo (1.0, 0.001));
    assertThat(f.type(), is("Fisher test"));
    assertThat(f.name(), is("test"));
  }
}
