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
package edu.dfci.cccb.mev.heatmap.domain.concrete;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PreDestroy;

import lombok.Synchronized;
import edu.dfci.cccb.mev.heatmap.domain.Heatmap;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapException;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapNotFoundException;
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractWorkspace;

/**
 * @author levk
 * 
 */
public class ListWorkspace extends AbstractWorkspace implements AutoCloseable {

  private final List<Heatmap> heatmaps = new ArrayList<> ();

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Workspace#get(java.lang.String) */
  @Override
  public Heatmap get (String id) throws HeatmapNotFoundException {
    ListIterator<Heatmap> found = find (id);
    if (found == null)
      throw new HeatmapNotFoundException ().id (id);
    else
      return found.previous ();
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.heatmap.domain.Workspace#put(edu.dfci.cccb.mev.heatmap
   * .domain.Heatmap) */
  @Override
  @Synchronized
  public void put (Heatmap heatmap) {
    Iterator<Heatmap> found = find (heatmap.name ());
    if (found != null)
      found.remove ();
    this.heatmaps.add (0, heatmap);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Workspace#list() */
  @Override
  public List<String> list () {
	  return extract (heatmaps, on (Heatmap.class).name ());
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.heatmap.domain.Workspace#remove(java.lang.String) */
  @Override
  @Synchronized
  public void remove (String id) throws HeatmapNotFoundException {
    Iterator<Heatmap> found = find (id);
    if (found == null)
      throw new HeatmapNotFoundException ().id (id);
    else
      found.remove ();
  }

  /* (non-Javadoc)
   * @see java.lang.AutoCloseable#close() */
  @Override
  @PreDestroy
  public void close () throws Exception {
    HeatmapException aggregator = new HeatmapException ("Aggregation of suppressed workspace closing exceptions") {
      private static final long serialVersionUID = 1L;
    };

    for (Heatmap heatmap : heatmaps)
      if (heatmap instanceof AutoCloseable)
        try {
          ((AutoCloseable) heatmap).close ();
        } catch (Exception e) {
          aggregator.addSuppressed (e);
        }

    if (aggregator.getSuppressed ().length > 0)
      throw aggregator;
  }

  private ListIterator<Heatmap> find (String id) {
    for (ListIterator<Heatmap> heatmaps = this.heatmaps.listIterator (); heatmaps.hasNext ();)
      if (heatmaps.next ().name ().equals (id))
        return heatmaps;
    return null;
  }
}
