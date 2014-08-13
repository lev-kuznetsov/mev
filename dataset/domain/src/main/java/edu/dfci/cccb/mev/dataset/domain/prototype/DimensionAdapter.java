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

import static edu.dfci.cccb.mev.dataset.domain.prototype.ValuesAdapter.all;
import static java.util.Arrays.asList;
import static lombok.AccessLevel.PROTECTED;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;

/**
 * @author levk
 */
@ToString
@Accessors (fluent = true)
@RequiredArgsConstructor (access = PROTECTED)
public abstract class DimensionAdapter <K> implements Dimension<K> {

  private final @Getter String name;

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.Dimension#iterator() */
  @Override
  public Iterator<K> iterator () {
    return new Iterator<K> () {
      private int index = 0;

      @Override
      public boolean hasNext () {
        return index < size ();
      }

      @Override
      public K next () {
        return get (index++);
      }

      @Override
      public void remove () {
        throw new UnsupportedOperationException ();
      }
    };
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode() */
  @Override
  public int hashCode () {
    int result = 0;
    for (K key : this)
      result ^= key.hashCode ();
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object) */
  @Override
  public boolean equals (Object obj) {
    if (obj instanceof Dimension) {
      Dimension<?> other = (Dimension<?>) obj;
      if (other.size () != size ())
        return false;
      Iterator<?> that = other.iterator ();
      Iterator<K> current = iterator ();
      while (that.hasNext () && current.hasNext () && (current.next ().equals (that.next ())));
      return true;
    } else
      return false;
  }

  /**
   * Wraps a set of dimensions into a map
   * 
   * @param dimensions
   * @return
   */
  @SafeVarargs
  public static <K> Map<String, Dimension<K>> dimensions (final Dimension<K>... dimensions) {
    return new AbstractMap<String, Dimension<K>> () {
      private final List<Dimension<K>> list = new ArrayList<Dimension<K>> (asList (dimensions));

      @Override
      public Set<Entry<String, Dimension<K>>> entrySet () {
        return new AbstractSet<Entry<String, Dimension<K>>> () {

          @Override
          public Iterator<Entry<String, Dimension<K>>> iterator () {
            return new Iterator<Entry<String, Dimension<K>>> () {
              private int index = 0;

              @Override
              public boolean hasNext () {
                return index < list.size ();
              }

              @Override
              public Entry<String, Dimension<K>> next () {
                return new Entry<String, Dimension<K>> () {
                  private final int current = index++;

                  @Override
                  public String getKey () {
                    return list.get (current).name ();
                  }

                  @Override
                  public Dimension<K> getValue () {
                    return list.get (current);
                  }

                  @Override
                  public Dimension<K> setValue (Dimension<K> value) {
                    Dimension<K> previous = getValue ();
                    if (previous.name ().equals (value.name ()))
                      list.set (current, value);
                    else
                      throw new IllegalArgumentException ();
                    return previous;
                  }
                };
              }

              @Override
              public void remove () {
                throw new UnsupportedOperationException ();
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
      public Dimension<K> put (String key, Dimension<K> value) {
        if (key == null || value == null || value.name () == null)
          throw new NullPointerException ();
        for (Entry<String, Dimension<K>> entry : entrySet ())
          if (entry.getKey ().equals (key))
            return entry.setValue (value);
        throw new IllegalArgumentException ();
      }
    };
  }

  /**
   * Iterates over all coordinates in a given projection
   * 
   * @param target
   * @param space
   * @return
   */
  @SafeVarargs
  public static <K> Iterable<Map<String, K>> project (final String dimension, final K key, final Dimension<K>... space) {
    return new Iterable<Map<String, K>> () {
      @Override
      public Iterator<Map<String, K>> iterator () {
        return new Iterator<Map<String, K>> () {
          private final Iterator<Map<String, K>> iterated = all (space).iterator ();

          @Override
          public boolean hasNext () {
            return iterated.hasNext ();
          }

          @Override
          public Map<String, K> next () {
            Map<String, K> next = iterated.next ();
            next.put (dimension, key);
            return next;
          }

          @Override
          public void remove () {
            throw new UnsupportedOperationException ();
          }
        };
      }
    };
  }
}
