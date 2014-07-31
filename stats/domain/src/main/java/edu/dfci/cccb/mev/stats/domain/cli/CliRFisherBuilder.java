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
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.io.implementation.TemporaryFile;
import edu.dfci.cccb.mev.stats.domain.contract.Fisher;
import edu.dfci.cccb.mev.stats.domain.prototype.AbstractFisherBuilder;

/**
 * @author levk
 * 
 */
@Accessors (chain = true)
public class CliRFisherBuilder extends AbstractFisherBuilder {

  private @Getter @Setter @Inject @Named ("R") ScriptEngine r;

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder#build() */
  @Override
  public Fisher build () throws DatasetException {
    try (TemporaryFile fisher = new TemporaryFile ();
         ByteArrayOutputStream script = new ByteArrayOutputStream ();
         PrintStream s = new PrintStream (script)) {

      s.println ("M=" + m ());
      s.println ("N=" + n ());
      s.println ("S=" + s ());
      s.println ("T=" + t ());
      s.println ("ALT_HYPOTHESIS=\"" + hypothesis () + "\"");
      s.println ("IS_SIMULATED=" + valueOf (simulate ()).toString ().toUpperCase ());
      s.println ("FISHER_OUT=\"" + fisher.getAbsolutePath () + "\"");

      copy (getClass ().getResourceAsStream ("/fisher.r"), s);

      r.eval (script.toString ());

      try (BufferedReader reader = new BufferedReader (new FileReader (fisher))) {
        reader.readLine ();
        final double oddRatio = Double.parseDouble (reader.readLine ().split ("\t")[1]);
        final double pValue = Double.parseDouble (reader.readLine ().split ("\t")[1]);

        return new PojoFisher (oddRatio, pValue).name(this.name()).type(type());
      }
    } catch (IOException | ScriptException e) {
      throw new DatasetException (e);
    }
  }
}
