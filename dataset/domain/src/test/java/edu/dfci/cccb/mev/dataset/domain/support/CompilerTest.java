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

package edu.dfci.cccb.mev.dataset.domain.support;

import static edu.dfci.cccb.mev.dataset.domain.support.Compiler.DOUBLE;
import static edu.dfci.cccb.mev.dataset.domain.support.Compiler.STRING;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

public class CompilerTest {

  @Test
  public void stringEmitter () throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream ();
    STRING.emit ("hello world", out);
    assertThat (out.toString (), is ("\"hello world\""));
  }

  @Test
  public void doubleEmitter () throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream ();
    DOUBLE.emit (.1, out);
    assertThat (Double.valueOf (out.toString ()), is (closeTo (.1, .0001)));
  }

  @Test
  public void doubleEmitterSpecialValues () throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream ();
    DOUBLE.emit (Double.NaN, out);
    assertThat (out.toString (), is ("NA"));
  }
}
