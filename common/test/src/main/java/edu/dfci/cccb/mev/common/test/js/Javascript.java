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

package edu.dfci.cccb.mev.common.test.js;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static org.reflections.util.ClasspathHelper.forPackage;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import lombok.Getter;
import lombok.experimental.Accessors;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ConfigurationBuilder;

/**
 * JSR-223 javascript engine builder
 * 
 * @author levk
 * @since CRYSTAL
 */
@Accessors (fluent = true)
public class Javascript {

  /**
   * Default scan root
   */
  public static final String ROOT = "/META-INF/resources";

  /**
   * Javascript engine
   */
  private @Getter final ScriptEngine engine = new ScriptEngineManager ().getEngineByName ("javascript");

  {
    if (engine == null)
      throw new IllegalStateException ();
  }

  /**
   * Shorthand to import
   */
  public static Javascript js () {
    return new Javascript ();
  }

  private Iterable<String> possibles (String script) {
    return asList (script,
                   script + ".js",
                   "/" + script,
                   "/" + script + ".js",
                   (ROOT + "/" + script).replaceAll ("//", "/"),
                   (ROOT + "/" + script).replaceAll ("//", "/") + ".js",
                   (ROOT + "/javascript/" + script).replaceAll ("//", "/"),
                   (ROOT + "/javascript/" + script).replaceAll ("//", "/") + ".js");
  }

  /**
   * @param scripts
   * @return
   * @throws ScriptException
   */
  public Javascript with (String... scripts) throws ScriptException {
    return with (asList (scripts));
  }

  /**
   * @param scripts
   * @return
   * @throws ScriptException
   */
  public Javascript with (Iterable<String> scripts) throws ScriptException {
    next: for (String script : scripts) {
      for (String possible : possibles (script)) {
        InputStream input = getClass ().getResourceAsStream (possible);
        if (input != null) {
          engine.eval (new InputStreamReader (input));
          continue next;
        }
      }
      throw new IllegalArgumentException ("Unable to find suitable candidate for script " + script);
    }
    return this;
  }

  /**
   * @param bases
   * @return
   * @throws ScriptException
   */
  public Javascript scan (String... bases) throws ScriptException {
    if (bases.length < 1)
      bases = new String[] { ROOT };

    ConfigurationBuilder configuration = new ConfigurationBuilder ().addScanners (new ResourcesScanner ());
    for (String base : bases)
      configuration.addUrls (forPackage (base));

    return with (new Reflections (configuration).getResources (compile (".*\\.js")).toArray (new String[0]));
  }
}
