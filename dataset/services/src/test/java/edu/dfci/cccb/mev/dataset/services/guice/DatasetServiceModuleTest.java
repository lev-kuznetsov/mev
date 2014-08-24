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

package edu.dfci.cccb.mev.dataset.services.guice;

import static com.google.inject.name.Names.named;
import static edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter.WORKSPACE;

import java.util.Map;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.guice.DatasetModule;

public class DatasetServiceModuleTest {

  @Test
  public void workspace () throws Exception {
    Guice.createInjector (new DatasetModule (), new DatasetServiceModule ())
         .getBinding (Key.get (new TypeLiteral<Map<String, Dataset<String, Double>>> () {},
                               named (WORKSPACE)));
  }
}
