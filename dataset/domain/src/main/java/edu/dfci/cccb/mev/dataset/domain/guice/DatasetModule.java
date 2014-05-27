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

import static edu.dfci.cccb.mev.dataset.domain.prototype.AnalysisAdapter.analyses;
import static edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter.dimensions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

import edu.dfci.cccb.mev.common.domain.guice.MevDomainModule;
import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;
import edu.dfci.cccb.mev.common.domain.guice.jackson.JacksonSerializerBinder;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.DimensionAdapter;
import edu.dfci.cccb.mev.dataset.domain.support.Builder;
import edu.dfci.cccb.mev.dataset.domain.support.Consumer;
import edu.dfci.cccb.mev.dataset.domain.support.jooq.JooqDataSourceStoreValuesAdapter;
import edu.dfci.cccb.mev.dataset.domain.support.json.DatasetJsonSerializer;
import edu.dfci.cccb.mev.dataset.domain.support.tsv.TsvParser;

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
        binder.bind (new TypeLiteral<Builder<String, Double>> () {}).toInstance (new Builder<String, Double> () {
          private @Inject DataSource dataSource;

          @SuppressWarnings ("unchecked")
          @Override
          public Dataset<String, Double> build (String name, InputStream input) throws Exception {
            JooqDataSourceStoreValuesAdapter values = new JooqDataSourceStoreValuesAdapter (dataSource);
            final List<String> columns = new ArrayList<> ();
            final List<String> rows = new ArrayList<> ();
            new TsvParser ().parse (input,
                                    values.builder (),
                                    (Consumer<String>[]) new Consumer<?>[] { new Consumer<String> () {

                                      @Override
                                      public void consume (String entity) throws IOException {
                                        rows.add (entity);
                                      }
                                    }, new Consumer<String> () {

                                      @Override
                                      public void consume (String entity) throws IOException {
                                        columns.add (entity);
                                      }
                                    } });
            return new DatasetAdapter<String, Double> (name,
                                                       dimensions (new DimensionAdapter<String> ("row") {

                                                         @Override
                                                         public int size () {
                                                           return rows.size ();
                                                         }

                                                         @Override
                                                         public String get (int index) {
                                                           return rows.get (index);
                                                         }
                                                       }, new DimensionAdapter<String> ("column") {

                                                         @Override
                                                         public int size () {
                                                           return columns.size ();
                                                         }

                                                         @Override
                                                         public String get (int index) {
                                                           return columns.get (index);
                                                         }
                                                       }),
                                                       analyses (),
                                                       values);
          }
        });
      }
    });
  }
}
