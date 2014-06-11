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

package edu.dfci.cccb.mev.common.web.servlet;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

import lombok.Delegate;

import org.junit.Test;

public class RequireInjectorServletTest {

  @Test
  public void configuration () throws Exception {
    assertTrue (RequireInjectorServlet.configuration ().get ("paths").isObject ());
  }

  @Test
  public void servlet () throws Exception {
    HttpServletResponse response = mock (HttpServletResponse.class);
    final ByteArrayOutputStream content = new ByteArrayOutputStream ();
    when (response.getOutputStream ()).thenReturn (new ServletOutputStream () {

      private final @Delegate ByteArrayOutputStream delegate = content;

      public boolean isReady () {
        return true;
      }

      public void setWriteListener (WriteListener writeListener) {}
    });

    new RequireInjectorServlet ().doGet (null, response);

    ScriptEngine js = new ScriptEngineManager ().getEngineByExtension ("js");
    js.eval (new StringReader ("define = function (deps, action) { return action (); }"));
    js.eval (new StringReader ("require = " + content.toString ()));
    assertThat (js.eval (new StringReader ("require.paths")), is (notNullValue ()));
  }
}
