package edu.dfci.cccb.mev.deseq.domain.cli;

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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.script.ScriptEngineManager;

import lombok.extern.log4j.Log4j;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvComposerFactory;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.deseq.domain.contract.DESeq;
import edu.dfci.cccb.mev.deseq.domain.contract.DESeq.Entry;
import edu.dfci.cccb.mev.deseq.domain.simple.StatelessScriptEngineFileBackedDESeqBuilder;

/**
 * @author levk
 * 
 */
@Log4j
public class CliRDESeqTest {

  @Test @Ignore
  public void test () throws Exception {
    try (InputStream inp = getClass ().getResourceAsStream ("/test_data.tsv");
         ByteArrayOutputStream copy = new ByteArrayOutputStream ()) {
      IOUtils.copy (inp, copy);
      Dataset dataset = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
                                                   .setValueStoreBuilder (new MapBackedValueStoreBuilder ())
                                                   .build (new MockTsvInput ("mock",
                                                                             copy.toString ()));
      Selection experiment = new SimpleSelection ("experiment", new Properties (), asList ("SA1_06_25_14",
                                                                                           "SA2_06_25_14",
                                                                                           "SA3_06_25_14",
                                                                                           "SA4_006_25_14","SA5_06_25_14"
));
      Selection control = new SimpleSelection ("control", new Properties (), asList ("SA10-06-25-14", "SA11_06_25_14", "SA12_06_25_14"));
      dataset.dimension (COLUMN).selections ().put (experiment);
      dataset.dimension (COLUMN).selections ().put (control);
      DESeq result =
                     new StatelessScriptEngineFileBackedDESeqBuilder ().r (new ScriptEngineManager ().getEngineByName ("CliR"))
                                                                       .composerFactory (new SuperCsvComposerFactory ())
                                                                       .dataset (dataset)
                                                                       .control (control)
                                                                       .experiment (experiment)
                                                                       .build ();
      for (Entry e : result.full ())
        log.debug ("Full DESeq entry: " + e);
    }
  }
}
