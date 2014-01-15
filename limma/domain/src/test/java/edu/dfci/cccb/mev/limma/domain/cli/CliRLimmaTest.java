package edu.dfci.cccb.mev.limma.domain.cli;

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

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.script.ScriptEngineManager;

import lombok.extern.log4j.Log4j;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvComposerFactory;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.limma.domain.contract.Limma;
import edu.dfci.cccb.mev.limma.domain.contract.Limma.Entry;
import edu.dfci.cccb.mev.limma.domain.simple.StatelessScriptEngineFileBackedLimmaBuilder;

/**
 * @author levk
 * 
 */
@Log4j
public class CliRLimmaTest {

  @Test
  public void test () throws Exception {
    Dataset dataset = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
                                                 .setValueStoreBuilder (new MapBackedValueStoreBuilder ())
                                                 .build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\tsd\tse\tsf\n" +
                                                                                   "g1\t.1\t.2\t.3\t.4\t.5\t.6\n" +
                                                                                   "g2\t.4\t.5\t.6\t.7\t.8\t.9"));
    Selection experiment = new SimpleSelection ("experiment", new Properties (), asList ("sa", "sc", "sd"));
    Selection control = new SimpleSelection ("control", new Properties (), asList ("sb", "se", "sf"));
    dataset.dimension (COLUMN).selections ().put (experiment);
    dataset.dimension (COLUMN).selections ().put (control);
    Limma result =
                   new StatelessScriptEngineFileBackedLimmaBuilder ().r (new ScriptEngineManager ().getEngineByName ("CliR"))
                                                                     .composerFactory (new SuperCsvComposerFactory ())
                                                                     .dataset (dataset)
                                                                     .control (control)
                                                                     .experiment (experiment)
                                                                     .build ();
    for (Entry e : result.full ())
      log.debug ("Full limma entry: " + e);
    assertEquals (.35,
                  result.full ().iterator ().next ().averageExpression (),
                  .1);
  }
}
