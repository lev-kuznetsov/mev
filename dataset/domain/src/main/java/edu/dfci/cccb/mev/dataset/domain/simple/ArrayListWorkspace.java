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

import static edu.dfci.cccb.mev.dataset.domain.support.LifecycleUtilities.destroy;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Synchronized;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractWorkspace;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode (callSuper = true)
@ToString
@Log4j
public class ArrayListWorkspace extends AbstractWorkspace implements AutoCloseable {

  private final ArrayList<Dataset> datasets = new ArrayList<> ();

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.Workspace#put(edu.dfci.cccb.mev.dataset
   * .domain.Dataset) */
  @Override
  @Synchronized
  public void put (Dataset dataset) {
    if (log.isDebugEnabled ())
      log.debug ("Adding dataset " + dataset);
    for (Iterator<Dataset> datasets = this.datasets.iterator (); datasets.hasNext ();) {
      Dataset curDataset = datasets.next ();
      if (curDataset != null && dataset.name ().equals (curDataset.name ()))
        datasets.remove ();
    }
    datasets.add (0, dataset);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.Workspace#get(java.lang.String) */
  @Override
  public Dataset get (String name) throws DatasetNotFoundException {
    for (Dataset dataset : datasets)
      if (dataset.name ().equals (name))
        return dataset;
    throw new DatasetNotFoundException ().name (name);
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

  private boolean hasSubsets(Dataset dataset){
    for(String name : list()){
      if(name.startsWith(dataset.name()+"--"))
        return true;
    }
    return false;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.Workspace#remove(java.lang.String) */
  @Override
  @Synchronized
  public void remove (String name) throws DatasetNotFoundException {
    for (Iterator<Dataset> datasets = this.datasets.iterator (); datasets.hasNext ();) {
      Dataset dataset = datasets.next ();
      if (dataset.name ().equals (name)) {
        datasets.remove ();
        try {
          if(!hasSubsets(dataset))
            destroy (new Exception ("Failure to destroy dataset on removal from workspace"), dataset);
        } catch (Exception e) {
          log.warn (e);
        }
        return;
      }
    }
    throw new DatasetNotFoundException ().name (name);
  }

  /* (non-Javadoc)
   * @see java.lang.AutoCloseable#close() */
  @Override
  public void close () throws Exception {
    destroy (new Exception ("Failure to close workspace"), datasets);
  }
}
