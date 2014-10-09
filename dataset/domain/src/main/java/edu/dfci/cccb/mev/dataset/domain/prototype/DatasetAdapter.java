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

package edu.dfci.cccb.mev.dataset.domain.prototype;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.Analysis;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.Values;

/**
 * Common {@link Dataset} implementation
 * 
 * @author levk
 * @since CRYSTAL
 */
@Accessors (fluent = true)
@EqualsAndHashCode
@ToString
public class DatasetAdapter <K, V> implements Dataset<K, V> {

  private final @Getter String name;
  private final @Getter Map<String, Dimension<K>> dimensions;
  private final @Getter Map<String, Analysis> analyses;
  private final @Getter Values<K, V> values;

  public DatasetAdapter (String name,
                         Map<String, Dimension<K>> dimensions,
                         Map<String, Analysis> analyses,
                         Values<K, V> values) {
    this.name = name;
    this.dimensions = dimensions;
    this.analyses = analyses;
    this.values = values;
  }

  @Path ("/dataset")
  public static abstract class Workspace <K, V> extends AbstractMap<String, Dataset<K, V>> {
    @GET
    @Override
    public Collection<Dataset<K, V>> values () {
      return super.values ();
    }

    @Path ("/{" + DATASET + "}")
    @GET
    public Dataset<K, V> get (@PathParam (DATASET) String key) {
      return super.get (key);
    }

    @Path ("/{" + DATASET + "}")
    @PUT
    @Override
    public Dataset<K, V> put (@PathParam (DATASET) String name, Dataset<K, V> value) {
      return super.put (name, value);
    }

    @Path ("/{" + DATASET + "}")
    @DELETE
    public Dataset<K, V> remove (@PathParam (DATASET) String key) {
      return super.remove (key);
    }
  }

  /**
   * @return A workspace for holding datasets
   */
  public static <K, V> Workspace<K, V> workspace () {
    return new Workspace<K, V> () {
      private final List<Dataset<K, V>> list = new ArrayList<> ();

      @Override
      public Set<Entry<String, Dataset<K, V>>> entrySet () {
        return new AbstractSet<Entry<String, Dataset<K, V>>> () {

          @Override
          public Iterator<Entry<String, Dataset<K, V>>> iterator () {
            return new Iterator<Entry<String, Dataset<K, V>>> () {
              private ListIterator<Dataset<K, V>> iterator = list.listIterator ();

              @Override
              public boolean hasNext () {
                return iterator.hasNext ();
              }

              @Override
              public Entry<String, Dataset<K, V>> next () {
                return new Entry<String, Dataset<K, V>> () {
                  private final Dataset<K, V> dataset = iterator.next ();

                  @Override
                  public Dataset<K, V> setValue (Dataset<K, V> value) {
                    Dataset<K, V> previous = getValue ();
                    if (getKey ().equals (value.name ()))
                      iterator.set (value);
                    else
                      throw new IllegalArgumentException ();
                    return previous;
                  }

                  @Override
                  public Dataset<K, V> getValue () {
                    return dataset;
                  }

                  @Override
                  public String getKey () {
                    return getValue ().name ();
                  }
                };
              }

              @Override
              
              public void remove () {
                iterator.remove ();
              }
            };
          }

          @Override
          public int size () {
            return list.size ();
          }
        };
      }

      @Override
      public Dataset<K, V> put (String name, Dataset<K, V> value) {
        if (name == null || value == null || value.name () == null)
          throw new NullPointerException ();
        for (Entry<String, Dataset<K, V>> entry : entrySet ())
          if (entry.getKey ().equals (name))
            return entry.setValue (value);
        if (!name.equals (value.name ()))
          throw new IllegalArgumentException ();
        list.add (0, value);
        return null;
      }
    };
  }
}
