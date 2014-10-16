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

import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.inject.Singleton;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;

import edu.dfci.cccb.mev.dataset.domain.Analysis;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.annotation.NameOf;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;

public class DatasetModuleTest {

  private Injector injector;

  @Before
  public void setUpInjector () {
    injector = Guice.createInjector (new DatasetModule (), new Module () {

      @Provides
      @Singleton
      @NameOf (Dataset.class)
      public String dataset () {
        return "mock";
      }

      @Provides
      @Singleton
      @NameOf (Analysis.class)
      public String analysis () {
        return "mock";
      }

      @Provides
      @Singleton
      @NameOf (Dimension.class)
      public String dimension () {
        return "mock";
      }

      @Provides
      @Singleton
      public Map<String, Dataset<String, Double>> workspace () {
        return DatasetAdapter.workspace ();
      }

      @Override
      public void configure (Binder binder) {}
    });
  }

  @Test
  public void serializers () {
    assertTrue (injector.getInstance (ObjectMapper.class).canSerialize (Dataset.class));
  }
}
