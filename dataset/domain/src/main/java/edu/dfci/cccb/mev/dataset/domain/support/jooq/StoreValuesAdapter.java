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

package edu.dfci.cccb.mev.dataset.domain.support.jooq;

import static org.jooq.impl.DSL.fieldByName;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.jooq.Field;

import edu.dfci.cccb.mev.common.domain.jooq.Store;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateSetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.support.Consumer;

/**
 * @author levk
 * @since CRYSTAL
 */
public class StoreValuesAdapter implements Values<String, Double>, AutoCloseable {

  private static final Field<String> ROW = fieldByName (String.class, "mev_row");
  private static final Field<String> COLUMN = fieldByName (String.class, "mev_column");
  private static final Field<Double> VALUE = fieldByName (Double.class, "mev_value");

  private static class ValueStore extends Store implements Consumer<Value<String, Double>> {

    ValueStore (String name, DataSource dataSource) throws SQLException {
      super (dataSource,
             name,
             new Field<?>[] { ROW, COLUMN, VALUE },
             new String[] { ROW.getName (), COLUMN.getName () });
    }

    ValueStore (DataSource dataSource) throws SQLException {
      super (dataSource,
             new Field<?>[] { ROW, COLUMN, VALUE },
             new String[] { ROW.getName (), COLUMN.getName () });
    }

    public double get (String row, String column) {
      return context ().select ()
                       .from (table ())
                       .where (ROW.equal (row))
                       .and (COLUMN.equal (column))
                       .fetchOne (VALUE);
    }

    /* (non-Javadoc)
     * @see
     * edu.dfci.cccb.mev.dataset.domain.support.Consumer#consume(java.lang.
     * Object) */
    @Override
    public void consume (Value<String, Double> entity) throws IOException {
      context ().insertInto (table ())
                .set (ROW, entity.coordinates ().get ("row"))
                .set (COLUMN, entity.coordinates ().get ("column"))
                .set (VALUE, entity.value ())
                .execute ();
    }
  }

  private ValueStore store;

  @Inject
  public StoreValuesAdapter (DataSource dataSource) throws SQLException {
    store = new ValueStore (dataSource);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Values#get(java.lang.Iterable) */
  @Override
  public Iterable<Value<String, Double>> get (final Iterable<Map<String, String>> coordinates) throws InvalidCoordinateSetException {
    return new Iterable<Value<String, Double>> () {
      @Override
      public Iterator<Value<String, Double>> iterator () {
        return new Iterator<Value<String, Double>> () {
          private final Iterator<Map<String, String>> iterator = coordinates.iterator ();

          @Override
          public boolean hasNext () {
            return iterator.hasNext ();
          }

          @Override
          public Value<String, Double> next () {
            Map<String, String> coordinate = iterator.next ();
            return new Value<String, Double> (store.get (coordinate.get ("row"), coordinate.get ("column")), coordinate);
          }

          @Override
          public void remove () {
            throw new UnsupportedOperationException ();
          }
        };
      }
    };
  }

  public Consumer<Value<String, Double>> builder () {
    return store;
  }

  /* (non-Javadoc)
   * @see java.lang.AutoCloseable#close() */
  @Override
  public void close () throws Exception {
    store.close ();
  }
}
