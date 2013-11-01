/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.heatmap.domain.mock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombok.Synchronized;

import edu.dfci.cccb.mev.heatmap.domain.Interval;
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractAnnotation;

/**
 * @author levk
 * 
 */
public class MockAnnotation extends AbstractAnnotation {

  private final Map<Integer, Map<String, Object>> annotations = new HashMap<> ();

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.heatmap.domain.Annotation#interval(edu.dfci.cccb.mev
   * .heatmap.domain.Interval) */
  @Override
  public Result interval (final Interval interval) {
    return new AbstractResult () {

      @Override
      public Iterable<Integer> indices () {
        return new Iterable<Integer> () {

          @Override
          public Iterator<Integer> iterator () {
            return new Iterator<Integer> () {

              private int current = interval.start ();

              @Override
              public boolean hasNext () {
                return current < interval.end ();
              }

              @Override
              public Integer next () {
                return current++;
              }

              @Override
              public void remove () {}
            };
          }
        };
      }

      @SuppressWarnings ("unchecked")
      @Override
      public <T> T get (int index, String attribute) {
        Map<String, Object> attributes = annotations.get (index);
        return (T) (attributes != null ? attributes.get (attribute) : null);
      }
    };
  }

  @Synchronized
  public <T> MockAnnotation set (int index, String attribute, T value) {
    Map<String, Object> attributes = annotations.get (index);
    if (attributes == null)
      annotations.put (index, attributes = new HashMap<String, Object> ());
    attributes.put (attribute, value);
    return this;
  }

  public static MockAnnotation annotation () {
    return new MockAnnotation ();
  }
}
