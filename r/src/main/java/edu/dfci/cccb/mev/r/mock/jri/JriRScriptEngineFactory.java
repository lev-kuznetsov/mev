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
package edu.dfci.cccb.mev.r.mock.jri;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static javax.script.ScriptEngine.ENGINE;
import static javax.script.ScriptEngine.ENGINE_VERSION;
import static javax.script.ScriptEngine.LANGUAGE_VERSION;
import static javax.script.ScriptEngine.NAME;

import java.util.List;

import javax.script.ScriptEngine;

import lombok.Getter;
import lombok.ToString;
import edu.dfci.cccb.mev.r.prototype.AbstractRScriptEngineFactory;

/**
 * @author levk
 * 
 */
@ToString
public class JriRScriptEngineFactory extends AbstractRScriptEngineFactory {

  private @Getter final String engineName = "JriR";
  private @Getter final String engineVersion = "mock";
  private @Getter final List<String> names = unmodifiableList (asList ("JRI", "Jri", "jri", "R", "r"));
  private @Getter final String languageVersion = "2.15"; // TODO: see if I can
                                                         // figure this out at
                                                         // runtime

  /* (non-Javadoc)
   * @see javax.script.ScriptEngineFactory#getParameter(java.lang.String) */
  @Override
  public Object getParameter (String key) {
    if (ENGINE.equals (key))
      return getEngineName ();
    else if (ENGINE_VERSION.equals (key))
      return getEngineVersion ();
    else if (LANGUAGE_VERSION.equals (key))
      return getLanguageVersion ();
    else if (NAME.equals (key))
      return getNames ().get (0);
    else
      return super.getParameter (key);
  }

  /* (non-Javadoc)
   * @see javax.script.ScriptEngineFactory#getScriptEngine() */
  @Override
  public ScriptEngine getScriptEngine () {
    throw new UnsupportedOperationException ("nyi");
  }
}
