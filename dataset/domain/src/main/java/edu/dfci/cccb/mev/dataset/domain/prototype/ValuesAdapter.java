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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;

/**
 * Common {@link Values} definitions
 * 
 * @author levk
 * @since BAYLIE
 */
public abstract class ValuesAdapter <K, V> implements Values<K, V> {

  /**
   * @param dimensions
   * @return Iterable over coordinates described by all the dimensions
   */
  @SafeVarargs
  public static <K> Iterable<Map<String, K>> all (Dimension<K>... dimensions) {
    return all (asList (dimensions));
  }

  /**
   * @param dimensions
   * @return Iterable over coordinates described by all the dimensions
   */
  public static <K> Iterable<Map<String, K>> all (final Iterable<Dimension<K>> dimensions) {
    class Frame implements Entry<String, K> {
      private final Frame parent;

      private final List<Dimension<K>> remaining;
      private final Iterator<K> keys;

      private String dimension;
      private K key;

      private Frame (Frame parent, List<Dimension<K>> dimensions) {
        this.parent = parent;

        remaining = dimensions.subList (1, dimensions.size ());

        Dimension<K> dimension = dimensions.get (0);

        this.dimension = dimension.name ();
        keys = dimension.iterator ();
      }

      Frame (final Iterable<Dimension<K>> dimensions) {
        this (null, unmodifiableList (new ArrayList<Dimension<K>> () {
          private static final long serialVersionUID = 1L;

          {
            for (Dimension<K> dimension : dimensions)
              add (dimension);
          }
        }));
      }

      public Frame advance () {
        if (keys.hasNext ()) {
          key = keys.next ();
          if (remaining.size () > 0)
            return new Frame (this, remaining).advance ();
          else
            return this;
        } else if (parent != null)
          return parent.advance ();
        else
          return null;
      }

      public Map<String, K> coordinates () {
        return new LinkedHashMap<String, K> () {
          private static final long serialVersionUID = 1L;

          {
            collect (Frame.this);
          }

          private void collect (Frame frame) {
            if (frame != null) {
              collect (frame.parent);
              put (frame.getKey (), frame.getValue ());
            }
          }
        };
      }

      @Override
      public String getKey () {
        return dimension;
      }

      @Override
      public K getValue () {
        return key;
      }

      @Override
      public K setValue (K value) {
        throw new UnsupportedOperationException ();
      }
    }

    return new Iterable<Map<String, K>> () {
      @Override
      public Iterator<Map<String, K>> iterator () {
        return new Iterator<Map<String, K>> () {
          private Frame current;

          {
            current = new Frame (dimensions).advance ();
          }

          @Override
          public boolean hasNext () {
            return current != null;
          }

          @Override
          public Map<String, K> next () {
            Map<String, K> result = current.coordinates ();
            current = current.advance ();
            return result;
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
