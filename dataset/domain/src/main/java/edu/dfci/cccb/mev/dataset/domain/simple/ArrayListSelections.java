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
package edu.dfci.cccb.mev.dataset.domain.simple;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import lombok.SneakyThrows;
import lombok.Synchronized;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractSelections;

/**
 * @author levk
 * 
 */
public class ArrayListSelections extends AbstractSelections {

  private final ArrayList<Selection> selections = new ArrayList<> ();

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Selections#put(edu.dfci.cccb
   * .mev.dataset.domain.contract.Selection) */
  @Override
  @Synchronized
  @SneakyThrows (SelectionNotFoundException.class)
  public void put (Selection subset) {
    find (true, subset.name ()).remove ();
    selections.add (0, subset);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Selections#get(java.lang.String) */
  @Override
  public Selection get (String name) throws SelectionNotFoundException {
    return find (false, name).next ();
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Selections#remove(java.lang.
   * String) */
  @Override
  @Synchronized
  public void remove (String name) throws SelectionNotFoundException {
    find (false, name).remove ();
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.Selections#list() */
  @Override
  public Collection<String> list () {
    return new AbstractCollection<String> () {

      @Override
      public Iterator<String> iterator () {
        final Iterator<Selection> selections = ArrayListSelections.this.selections.iterator ();

        return new Iterator<String> () {

          @Override
          public boolean hasNext () {
            return selections.hasNext ();
          }

          @Override
          public String next () {
            return selections.next ().name ();
          }

          @Override
          public void remove () {
            selections.remove ();
          }
        };
      }

      @Override
      public int size () {
        return selections.size ();
      }
    };
  }

  private ListIterator<Selection> find (boolean quiet, String name) throws SelectionNotFoundException {
    for (ListIterator<Selection> selections = this.selections.listIterator (); selections.hasNext ();)
      if (selections.next ().name ().equals (name))
        return selections;
    if (quiet)
      return new ArrayList<Selection> () {
        private static final long serialVersionUID = 1L;

        {
          add (null);
        }
      }.listIterator ();
    else
      throw new SelectionNotFoundException (); // TODO: add args
  }
}
