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

package edu.dfci.cccb.mev.common.domain.jobs.r.annotation;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import lombok.SneakyThrows;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;

import edu.dfci.cccb.mev.common.domain.jobs.r.Deserializer;

/**
 * @author levk
 */
public @interface toJava {

  Class<? extends Deserializer> value ();

  public static class Primitives implements Deserializer {

    @Override
    @SneakyThrows (REXPMismatchException.class)
    public Object deserialize (final Object o) {
      if (!(o instanceof REXP))
        throw new IllegalArgumentException ("Unsupported " + (o == null ? null : o.getClass ()));
      final REXP r = (REXP) o;
      if (r.isNull ())
        return null;
      else if (r.isLogical ()) {
        Boolean[] result = new Boolean[r.length ()];
        boolean[] isTrue = ((REXPLogical) r).isTRUE ();
        boolean[] isFalse = ((REXPLogical) r).isFALSE ();
        for (int i = result.length; --i >= 0; result[i] = isTrue[i] ? TRUE : (isFalse[i] ? FALSE : null));
        return result.length == 1 ? result[0] : result;
      } else if (r.isNumeric ()) {
        boolean[] isNa = r.isNA ();
        if (r.isInteger ()) {
          Integer[] result = new Integer[r.length ()];
          for (int i = result.length; --i >= 0; result[i] = isNa[i] ? null : r.asIntegers ()[i]);
          return result.length == 1 ? result[0] : result;
        } else {
          Double[] result = new Double[r.length ()];
          for (int i = result.length; --i >= 0; result[i] = isNa[i] ? null : r.asDoubles ()[i]);
          return result.length == 1 ? result[0] : result;
        }
      } else if (r.isString ())
        return r.asStrings ();
      else if (r.isList ()) {
        return !r.asList ().isNamed () ? new AbstractList<Object> () {

          @Override
          @SneakyThrows (REXPMismatchException.class)
          public Object get (int index) {
            return deserialize (r.asList ().at (index));
          }

          @Override
          @SneakyThrows (REXPMismatchException.class)
          public int size () {
            return r.length ();
          }
        } : new AbstractMap<String, Object> () {

          @Override
          public Set<Entry<String, Object>> entrySet () {
            return new AbstractSet<Entry<String, Object>> () {

              @Override
              public Iterator<Entry<String, Object>> iterator () {
                return new Iterator<Entry<String, Object>> () {
                  private int current = 0;

                  @Override
                  public boolean hasNext () {
                    return current < size ();
                  }

                  @Override
                  public Entry<String, Object> next () {
                    return new Entry<String, Object> () {
                      private final int index = current;

                      @SuppressWarnings ("unchecked")
                      @Override
                      @SneakyThrows (REXPMismatchException.class)
                      public Object setValue (Object value) {
                        Object result = getValue ();
                        r.asList ().setElementAt (value, index);
                        return result;
                      }

                      @Override
                      @SneakyThrows (REXPMismatchException.class)
                      public Object getValue () {
                        return r.asList ().elementAt (index);
                      }

                      @Override
                      @SneakyThrows (REXPMismatchException.class)
                      public String getKey () {
                        return r.asList ().keyAt (index);
                      }
                    };
                  }

                  @Override
                  @SneakyThrows (REXPMismatchException.class)
                  public void remove () {
                    r.asList ().remove (current - 1);
                  }
                };
              }

              @Override
              @SneakyThrows (REXPMismatchException.class)
              public int size () {
                return r.length ();
              }
            };
          }
        };
      } else
        throw new IllegalArgumentException ("Unsupported REXP type " + r.getClass ());
    }
  }
}
