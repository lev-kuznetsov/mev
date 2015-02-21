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

package edu.dfci.cccb.mev.cluster.domain;

import static edu.dfci.cccb.mev.cluster.domain.Distance.DEFAULT;
import static javax.xml.bind.annotation.XmlAccessType.NONE;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.r.RAnalysisAdapter;

/**
 * Common clustering API
 * 
 * @author levk
 * @since CRYSTAL
 */
@XmlRootElement
@XmlAccessorType (NONE)
public abstract class ClusteringAdapter <K, V> extends RAnalysisAdapter<K, V> {

  /**
   * Distance metric
   */
  private @Parameter Distance distance = DEFAULT;

  /**
   * Clustering dimension
   */
  private @Parameter Dimension<K> dimension;

  /**
   * Represents a subset to apply clustering to
   * 
   * @author levk
   * @since CRYSTAL
   */
  @XmlRootElement
  @XmlAccessorType (NONE)
  private class Subset extends AbstractSet<K> {
    private Set<K> delegate = new HashSet<> ();

    @Override
    public Iterator<K> iterator () {
      return delegate.iterator ();
    }

    @Override
    public int size () {
      return delegate.size ();
    }

    @POST
    @Override
    public boolean addAll (Collection<? extends K> c) {
      return delegate.addAll (c);
    }

    @Override
    public void clear () {
      delegate.clear ();
    }
  }

  /**
   * Subset of keys to apply clustering to, if empty applies to entire dimension
   */
  private @Parameter final Set<K> subset = new Subset ();

  @Inject
  private void configure (Dataset<K, V> dataset) {
    dimension = dataset.dimensions ().get ("column");
    subset.addAll (dimension);
  }

  @Path ("/distance")
  @PUT
  @XmlElement (name = "distance", required = false)
  public void set (Distance distance) {
    this.distance = distance;
  }

  @Path ("/distance")
  @GET
  public Distance distance () {
    return distance;
  }

  @Path ("/subset")
  @PUT
  @XmlElement (name = "subset", required = false)
  public void set (Collection<K> subset) {
    this.subset.clear ();
    this.subset.addAll (subset);
  }

  @Path ("/subset")
  @GET
  public Collection<K> subset () {
    return subset;
  }

  @Path ("/dimension")
  @PUT
  @XmlElement (name = "dimension", required = false)
  public void set (String dimension) {
    this.dimension = dataset ().dimensions ().get (dimension);
  }

  @Path ("/dimension")
  @GET
  public Dimension<K> dimension () {
    return dimension;
  }
}
