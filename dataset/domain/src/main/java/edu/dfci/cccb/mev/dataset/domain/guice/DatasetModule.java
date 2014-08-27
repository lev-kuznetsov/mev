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

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.inject.Binder;
import com.google.inject.Module;

import edu.dfci.cccb.mev.common.domain.guice.MevDomainModule;
import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;
import edu.dfci.cccb.mev.dataset.domain.support.json.DatasetJsonSerializer;
import edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvDeserializer;
import edu.dfci.cccb.mev.dataset.domain.support.tsv.DatasetTsvSerializer;

/**
 * @author levk
 * @since CRYSTAL
 */
public class DatasetModule implements Module {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public final void configure (Binder binder) {
    binder.install (new MevDomainModule () {

      @Override
      public void configure (JacksonSerializerBinder binder) {
        binder.with ((Class<? extends JsonSerializer<?>>) DatasetJsonSerializer.class);
      }
    });

    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {
        binder.bind (DatasetTsvDeserializer.class).in (Singleton.class);
        binder.bind (DatasetTsvSerializer.class).in (Singleton.class);
      }
    });
  }
}
