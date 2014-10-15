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
package edu.dfci.cccb.mev.deseq.domain.simple;

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.values;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;

import javax.script.ScriptException;

import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;
import edu.dfci.cccb.mev.deseq.domain.contract.InvalidDESeqConfigurationException;
import edu.dfci.cccb.mev.deseq.domain.contract.DESeq;
import edu.dfci.cccb.mev.deseq.domain.prototype.AbstractDESeqBuilder;
import edu.dfci.cccb.mev.io.implementation.TemporaryFolder;

/**
 * @author levk
 * 
 */
@Log4j
public class StatelessScriptEngineFileBackedDESeqBuilder extends AbstractDESeqBuilder {

  public static String OUTPUT_FILENAME;
  public static String NORMALIZED_COUNTS_FILENAME;
  
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder#build() */
  @Override
  public DESeq build () throws DatasetException {
    Dimension dimension = null;
    for (Type of : values ())
      try {
        dataset ().dimension (of).selections ().get (control ().name ());
        dataset ().dimension (of).selections ().get (experiment ().name ());
        dimension = dataset ().dimension (of);
      } catch (InvalidDimensionTypeException | SelectionNotFoundException e) {}
    if (dimension == null)
      throw new InvalidDESeqConfigurationException ();

    try {
      TemporaryFolder tempDESeqFolder = new TemporaryFolder ();

      log.debug ("Using " + tempDESeqFolder.getAbsolutePath () + " for DESeq analysis");

      try {
        File datasetFile = new File (tempDESeqFolder, DATASET_FILENAME);
        try (OutputStream datasetOut = new FileOutputStream (datasetFile)) {
          composerFactory ().compose (dataset ()).write (datasetOut);
        }

        File annotationFile = new File (tempDESeqFolder, ANNOTATION_FILENAME);
        try (PrintStream configOut = new PrintStream (new FileOutputStream (annotationFile))) {
          for (int index = 0; index < dimension.keys ().size (); index++)
            if (control ().keys ().contains (dimension.keys ().get (index))
                && experiment ().keys ().contains (dimension.keys ().get (index)))
              throw new InvalidDESeqConfigurationException ();
            else if (experiment ().keys ().contains (dimension.keys ().get (index)))
              configOut.println (dimension.keys ().get (index) + "\t"+experiment ().name ());
            else if (control ().keys ().contains (dimension.keys ().get (index)))
              configOut.println (dimension.keys ().get (index) + "\t"+control ().name ());
            else
              //shouldn't reach here without exception..but is this the right exception to throw?
              throw new InvalidDESeqConfigurationException ();
        }

        String contrastPrefix = experiment().name()+CONTRAST_FLAG+control().name();
        OUTPUT_FILENAME = contrastPrefix+".csv";
        NORMALIZED_COUNTS_FILENAME = contrastPrefix + "."+NORMALIZED_COUNT_FILE_TAG + ".csv";

        File fullOutputFile = new File (tempDESeqFolder, OUTPUT_FILENAME);
        File normCountFile = new File (tempDESeqFolder, NORMALIZED_COUNTS_FILENAME);

        //write lines with paths, etc. to the appropriate file locations INTO the R file
        try (ByteArrayOutputStream script = new ByteArrayOutputStream ();
             PrintStream printScript = new PrintStream (script)) {
          printScript.println ("COUNT_MTX_FILE=\"" + datasetFile.getAbsolutePath () + "\"");
          printScript.println ("SAMPLE_FILE=\"" + annotationFile.getAbsolutePath () + "\"");
          printScript.println ("CONDITION_A=\"" + control().name() + "\"");
          printScript.println ("CONDITION_B=\"" + experiment().name() + "\"");
          printScript.println ("OUTPUT_FILE=\"" + fullOutputFile.getAbsolutePath () + "\"");
          printScript.println ("NORMALIZED_COUNTS_FILE=\"" + normCountFile.getAbsolutePath () + "\"");
          
          try (InputStream deseqScript = getClass ().getResourceAsStream ("/deseq_basic.R")) {
            for (int c; (c = deseqScript.read ()) >= 0; printScript.write (c));
            printScript.flush ();

            try (Reader injectedScript = new InputStreamReader (new ByteArrayInputStream (script.toByteArray ()))) {

              r ().eval (injectedScript);

              if (!log.isDebugEnabled ()) {
                datasetFile.delete ();
                annotationFile.delete ();
              }

              return new FileBackedDESeq (tempDESeqFolder).name (name ())
                                                .type (type ())
                                                .control (control ())
                                                .experiment (experiment ());
            } catch (ScriptException e) {
              if (log.isDebugEnabled ())
                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream ();
                     Writer debug = new BufferedWriter (new OutputStreamWriter (buffer));
                     Reader datasetReader = new FileReader (datasetFile);
                     Reader configReader = new FileReader (annotationFile)) {
                  debug.write ("DESeq script failed\nInput dataset:\n");
                  for (int c; (c = datasetReader.read ()) >= 0; debug.write (c));
                  debug.write ("\nConfiguration:\n");
                  for (int c; (c = configReader.read ()) >= 0; debug.write (c));
                  debug.flush ();
                  log.debug (buffer.toString (), e);
                }
              throw e;
            }
          }
        }
      } catch (IOException | InvalidDESeqConfigurationException | RuntimeException | ScriptException e) {
        try {
          if (!log.isDebugEnabled ())
            tempDESeqFolder.close ();
        } catch (IOException e2) {
          e.addSuppressed (e2);
        }
        throw e;
      }
    } catch (IOException | ScriptException e) {
      throw new DatasetException (e);
    }
  }
}
