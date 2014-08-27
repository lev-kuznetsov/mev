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

package edu.dfci.cccb.mev.dataset.domain.guice;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvDeserializer;
import edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvSerializer;

public class DatasetModuleTest {

  private Injector injector;

  @Before
  public void setUpInjector () {
    injector = Guice.createInjector (new DatasetModule ());
  }

  @Test
  public void serializers () {
    assertTrue (injector.getInstance (ObjectMapper.class).canSerialize (Dataset.class));
    assertThat (injector.getInstance (DatasetTsvDeserializer.class), is (notNullValue (DatasetTsvDeserializer.class)));
    assertThat (injector.getInstance (DatasetTsvSerializer.class), is (notNullValue (DatasetTsvSerializer.class)));
  }
}
