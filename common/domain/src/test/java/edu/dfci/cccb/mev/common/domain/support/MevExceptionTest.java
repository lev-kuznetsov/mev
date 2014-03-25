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

package edu.dfci.cccb.mev.common.domain.support;

import static com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class MevExceptionTest {

  @BeforeClass
  public static void prepareObjectMapper () {
    mapper.setAnnotationIntrospector (new JaxbAnnotationIntrospector (defaultInstance ()));
  }

  @BeforeClass
  public static void readExpectedValues () throws IOException {
    expected.load (MevExceptionTest.class.getResourceAsStream ("/MevException.expected.json"));
  }

  private static ObjectMapper mapper = new ObjectMapper ();
  private static Properties expected = new Properties ();

  private static class MevEImpl extends MevException {
    private static final long serialVersionUID = 1L;

    public MevEImpl () {}

    public MevEImpl (Throwable c) {
      super (c);
    }

    public MevEImpl (String m) {
      super (m);
    }
  }

  @Test
  public void noArg () throws Exception {
    assertThat (mapper.writeValueAsString (new MevEImpl ()),
                is ("{\"exception\":{\"exception.type\":\"edu.dfci.cccb.mev.common.domain.sup"
                    + "port.MevExceptionTest$MevEImpl\"}}"));
  }

  @Test
  public void withCause () throws Exception {
    assertThat (mapper.writeValueAsString (new MevEImpl (new MevEImpl ())),
                is ("{\"exception\":{\"exception.type\":\"edu.dfci.cccb.mev.common.domain.sup"
                    + "port.MevExceptionTest$MevEImpl\",\"exception.cause\":{\"exception"
                    + "\":{\"exception.type\":\"edu.dfci.cccb.mev.common.domain.support.M"
                    + "evExceptionTest$MevEImpl\"}}}}"));
  }

  @Test
  public void withMessage () throws Exception {
    assertThat (mapper.writeValueAsString (new MevEImpl ("hello")),
                is ("{\"exception\":{\"exception.type\":\"edu.dfci.cccb.mev.common.domain.sup"
                    + "port.MevExceptionTest$MevEImpl\",\"exception.message\":\"hello\"}}"));
  }

  @Test
  public void withNonMevCause () throws Exception {
    assertThat (mapper.writeValueAsString (new MevEImpl (new NullPointerException ())),
                is ("{\"exception\":{\"exception.type\":\"edu.dfci.cccb.mev.common.domain.sup"
                    + "port.MevExceptionTest$MevEImpl\",\"exception.cause\":\"java.lang.N"
                    + "ullPointerException:null\"}}"));
  }

  @Test
  public void withCustomProperty () throws Exception {
    assertThat (mapper.writeValueAsString (new MevEImpl () {
      private static final long serialVersionUID = 1L;

      {
        property ("hello", "world");
      }
    }), is ("{\"exception\":{\"hello\":\"world\",\"exception.type\":\"edu.dfci.cccb.mev.commo"
            + "n.domain.support.MevExceptionTest$1\"}}"));
  }
}
