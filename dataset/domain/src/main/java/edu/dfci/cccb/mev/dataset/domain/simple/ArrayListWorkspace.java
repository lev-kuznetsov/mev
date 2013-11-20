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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import lombok.SneakyThrows;
import lombok.Synchronized;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractWorkspace;

/**
 * @author levk
 * 
 */
public class ArrayListWorkspace extends AbstractWorkspace {

  private final ArrayList<Dataset> datasets = new ArrayList<> ();

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.Workspace#put(edu.dfci.cccb.mev.dataset
   * .domain.Dataset) */
  @Override
  @Synchronized
  @SneakyThrows (DatasetNotFoundException.class)
  public void put (Dataset dataset) {
    find (true, dataset.name ()).remove ();
    datasets.add (0, dataset);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.Workspace#get(java.lang.String) */
  @Override
  public Dataset get (String name) throws DatasetNotFoundException {
    return find (false, name).previous ();
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.Workspace#list() */
  @Override
  public List<String> list () {
    return new AbstractList<String> () {

      @Override
      public String get (int index) {
        return datasets.get (index).name ();
      }

      @Override
      public int size () {
        return datasets.size ();
      }
    };
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.Workspace#remove(java.lang.String) */
  @Override
  @Synchronized
  public void remove (String name) throws DatasetNotFoundException {
    find (false, name).remove ();
  }

  private ListIterator<Dataset> find (boolean quiet, String name) throws DatasetNotFoundException {
    for (ListIterator<Dataset> datasets = this.datasets.listIterator (); datasets.hasNext ();)
      if (datasets.next ().name ().equals (name))
        return datasets;
    if (quiet)
      return new ArrayList<Dataset> () {
        private static final long serialVersionUID = 1L;

        {
          add (null);
        }
      }.listIterator ();
    else
      throw new DatasetNotFoundException (); // TODO: add args
  }
}
