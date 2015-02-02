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

package edu.dfci.cccb.mev.dataset.domain.r;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.common.domain.jobs.r.R;
import edu.dfci.cccb.mev.dataset.domain.AnalysisInvocationException;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.prototype.AnalysisAdapter;

/**
 * @author levk
 * @since CRYSTAL
 */
public class RAnalysisAdapter <K, V> extends AnalysisAdapter {

  private @Inject R dispatcher;

  private @Inject @Parameter Dataset<K, V> dataset;

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.Analysis#run() */
  @Override
  public void run () throws AnalysisInvocationException {
    dispatcher.dispatch (this);
  }

  protected List<K> keys () {
    return new AbstractList<K> () {
      private List<K> delegate = new ArrayList<> ();

      @Override
      @GET
      @Path ("/{index}")
      public K get (@PathParam ("index") int index) {
        return delegate.get (index);
      }

      @Override
      @POST
      public boolean addAll (Collection<? extends K> c) {
        return delegate.addAll (c);
      }

      @Override
      @PUT
      @Path ("/{index}")
      public void add (@PathParam ("index") int index, K element) {
        delegate.add (index, element);
      }

      @Override
      @DELETE
      @Path ("index")
      public K remove (@PathParam ("index") int index) {
        return delegate.remove (index);
      }

      @Override
      @DELETE
      public boolean remove (Object o) {
        return delegate.remove (o);
      }

      @Override
      public int size () {
        return delegate.size ();
      }
    };
  }
}
