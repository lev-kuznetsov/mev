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

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.Analysis;

/**
 * @author levk
 * @since BAYLIE
 */
@Accessors (fluent = true)
public abstract class AnalysisAdapter implements Analysis {

  private @Getter @Setter String name;

  public static Map<String, Analysis> analyses () {
    return new AbstractMap<String, Analysis> () {
      private final List<Analysis> list = new ArrayList<> ();

      /* (non-Javadoc)
       * @see java.util.AbstractMap#values() */
      @GET
      @Override
      public Collection<Analysis> values () {
        return super.values ();
      }

      /* (non-Javadoc)
       * @see java.util.AbstractMap#get(java.lang.Object) */
      @Path ("/{" + ANALYSIS + "}")
      @GET
      public Analysis get (@PathParam (ANALYSIS) String key) {
        return super.get (key);
      }

      @Override
      public Set<Entry<String, Analysis>> entrySet () {
        return new AbstractSet<Entry<String, Analysis>> () {

          @Override
          public Iterator<Entry<String, Analysis>> iterator () {
            return new Iterator<Entry<String, Analysis>> () {
              private ListIterator<Analysis> iterator = list.listIterator ();

              @Override
              public boolean hasNext () {
                return iterator.hasNext ();
              }

              @Override
              public Entry<String, Analysis> next () {
                return new Entry<String, Analysis> () {
                  private final Analysis analysis = iterator.next ();

                  @Override
                  public Analysis setValue (Analysis value) {
                    Analysis previous = getValue ();
                    if (getKey ().equals (value.name ()))
                      iterator.set (value);
                    else
                      throw new IllegalArgumentException ();
                    return previous;
                  }

                  @Override
                  public Analysis getValue () {
                    return analysis;
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

      @Path ("/{" + ANALYSIS + "}")
      @PUT
      @Override
      public Analysis put (@PathParam (ANALYSIS) String name, Analysis analysis) {
        if (name == null || analysis == null || analysis.name () == null)
          throw new NullPointerException ();
        for (Entry<String, Analysis> entry : entrySet ())
          if (entry.getKey ().equals (name))
            return entry.setValue (analysis);
        if (!name.equals (analysis.name ()))
          throw new IllegalArgumentException ();
        list.add (0, analysis);
        return null;
      }
    };
  }
}
