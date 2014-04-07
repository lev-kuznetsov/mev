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

package edu.dfci.cccb.mev.common.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Before;
import org.junit.Test;

public class CommonJsTest {

  private ScriptEngine js;

  @Before
  public void setUpJs () throws Exception {
    js = new ScriptEngineManager ().getEngineByName ("javascript");
    js.eval (new InputStreamReader (getClass ().getResourceAsStream ("/META-INF/resources/javascript/domain/common.js")));
  }

  @Test
  public void deepEquals () throws Exception {
    assertThat (js.eval ("Object.deepEquals (JSON.parse ('{\"foo\":3,\"bar\":5}'), JSON.parse ('{\"bar\":5,\"foo\":3}'));")
                  .toString (),
                is ("true"));
  }
}
