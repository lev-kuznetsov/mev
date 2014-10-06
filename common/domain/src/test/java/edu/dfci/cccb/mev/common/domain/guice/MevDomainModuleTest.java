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

package edu.dfci.cccb.mev.common.domain.guice;

import static com.google.inject.Guice.createInjector;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;

import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;

public class MevDomainModuleTest {

  private Injector injector;

  @Before
  public void installDatasetModule () throws Exception {
    injector = createInjector (new MevModule () {
      public void configure (JacksonSerializerBinder binder) {
        assertThat (binder, is (notNullValue ()));
      }
    });
  }

  @Test
  public void jacksonMapper () throws Exception {
    assertThat (injector.getInstance (ObjectMapper.class), is (notNullValue ()));
  }

  @Test
  public void dataSource () throws Exception {
    assertThat (injector.getInstance (DataSource.class), is (notNullValue ()));
  }
}
