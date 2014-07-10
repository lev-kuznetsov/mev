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

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

import javax.script.ScriptEngineManager;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.stats.domain.contract.Wilcoxon;

/**
 * @author levk
 * 
 */
public class CliRWilcoxonTest {

  @Test
  public void two () throws Exception {
    Wilcoxon w =
                 new CliRWilcoxonBuilder ().setR (new ScriptEngineManager ().getEngineByName ("CliR"))
                                           .first ("sa")
                                           .second ("sb")
                                           .dataset (new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
                                                                                .setValueStoreBuilder (new MapBackedValueStoreBuilder ())
                                                                                .build (new MockTsvInput ("mock",
                                                                                                          "\tsa\tsb\tsc\n"
                                                                                                                  +
                                                                                                                  "g1\t.1\t.2\t.3\n"
                                                                                                                  +
                                                                                                                  "g2\t.2\t.3\t.4")))
                                           .build ();

    assertThat (w.pValue (), closeTo (.66667, 0.001));
    assertThat (w.wStat (), closeTo (0, 0.0001));
  }
  /* @Test public void one () throws Exception { new CliRWilcoxonBuilder ().setR
   * (new ScriptEngineManager ().getEngineByName ("CliR")) .first ("sa")
   * .dataset (new SimpleDatasetBuilder ().setParserFactories (asList (new
   * SuperCsvParserFactory ())) .setValueStoreBuilder (new
   * MapBackedValueStoreBuilder ()) .build (new MockTsvInput ("mock",
   * "\tsa\tsb\tsc\n" + "g1\t.1\t.2\t.3\n" + "g2\t.2\t.3\t.4"))) .build (); } */
}
