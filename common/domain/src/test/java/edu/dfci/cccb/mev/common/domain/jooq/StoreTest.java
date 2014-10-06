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

package edu.dfci.cccb.mev.common.domain.jooq;

import static org.hamcrest.Matchers.is;
import static org.jooq.impl.DSL.fieldByName;
import static org.junit.Assert.assertThat;

import org.jooq.Field;
import org.junit.Test;

import com.google.inject.Guice;

import edu.dfci.cccb.mev.common.domain.guice.MevModule;

public class StoreTest {

  public static abstract class TestStore extends Store {
    public TestStore (Field<?>[] fields, String[]... indices) {
      super (fields, indices);
    }

    public abstract void run () throws Exception;
  }

  @Test
  public void createInsertAndRetreive () throws Exception {
    try (TestStore s = new TestStore (new Field<?>[] { fieldByName (String.class, "NAME"),
                                                      fieldByName (Double.class, "FLOATER") },
                                      new String[] { "NAME" }) {
      public void run () throws Exception {
        assertThat (context ().insertInto (table ())
                              .set (field ("NAME"), "HALLO")
                              .set (field ("FLOATER"), .2)
                              .execute (), is (1));
        assertThat ((String) context ().select ().from (table ()).fetchOne (field ("NAME")), is ("HALLO"));
      }
    }) {
      Guice.createInjector (new MevModule ()).injectMembers (s);
      s.run ();
    }
  }
}
