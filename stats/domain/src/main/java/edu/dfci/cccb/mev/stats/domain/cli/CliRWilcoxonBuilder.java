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

import static java.lang.Boolean.valueOf;
import static org.apache.commons.io.IOUtils.copy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import ch.lambdaj.Lambda;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.io.implementation.TemporaryFile;
import edu.dfci.cccb.mev.stats.domain.contract.Wilcoxon;
import edu.dfci.cccb.mev.stats.domain.prototype.AbstractWilcoxonBuilder;

/**
 * @author levk
 * 
 */
@Accessors (chain = true)
public class CliRWilcoxonBuilder extends AbstractWilcoxonBuilder {

  private @Getter @Setter @Inject @Named ("R") ScriptEngine r;

  @SuppressWarnings ("unchecked")
  private List<Double> column (final String column) {
    return column != null ? new AbstractList<Double> () {

      @Override
      @SneakyThrows (DatasetException.class)
      public Double get (int index) {
        return dataset ().values ().get (dataset ().dimension (Type.ROW).keys ().get (index), column);
      }

      @Override
      @SneakyThrows (InvalidDimensionTypeException.class)
      public int size () {
        return dataset ().dimension (Type.ROW).keys ().size ();
      }
    } : ((List<Double>) Collections.EMPTY_LIST);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder#build() */
  @Override
  public Wilcoxon build () throws DatasetException {
    try (TemporaryFile sample1 = new TemporaryFile ();
         TemporaryFile sample2 = new TemporaryFile ();
         TemporaryFile wilcoxon = new TemporaryFile ();
         ByteArrayOutputStream script = new ByteArrayOutputStream ();
         PrintStream s = new PrintStream (script)) {

      try (PrintStream s1 = new PrintStream (new FileOutputStream (sample1));
           PrintStream s2 = new PrintStream (new FileOutputStream (sample2))) {
        s1.println (Lambda.join (column (first ()), "\t"));
        if (second () != null)
          s2.println (Lambda.join (column (second ()), "\t"));
      }

      s.println ("SAMPLE1_FILE=\"" + sample1.getAbsolutePath () + "\"");
      s.println ("SAMPLE2_FILE=" + (second () == null ? "NULL" : ("\"" + sample2.getAbsolutePath () + "\"")));
      s.println ("ALT_HYPOTHESIS=\"" + hypothesis () + "\"");
      s.println ("IS_PAIRED=" + valueOf (pair ()).toString ().toUpperCase ());
      s.println ("IS_CONF_INT=" + valueOf (confidentInterval ()).toString ().toUpperCase ());
      s.println ("WILCOX_OUT=\"" + wilcoxon.getAbsolutePath () + "\"");

      copy (getClass ().getResourceAsStream ("/wilcoxon.r"), s);

      r.eval (script.toString ());

      try (BufferedReader reader = new BufferedReader (new FileReader (wilcoxon))) {
        reader.readLine ();
        final double wStat = Double.parseDouble (reader.readLine ().split ("\t")[1]);
        final double pValue = Double.parseDouble (reader.readLine ().split ("\t")[1]);

        return new PojoWilcoxon (wStat, pValue);
      }
    } catch (IOException | ScriptException e) {
      throw new DatasetException (e);
    }
  }
}
