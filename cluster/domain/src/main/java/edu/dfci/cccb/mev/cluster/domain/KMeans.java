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

import static javax.xml.bind.annotation.XmlAccessType.NONE;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Result;
import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.annotation.Analysis;

/**
 * @author levk
 * @since CRYSTAL
 */
@Analysis ("kmeans")
@XmlRootElement
@XmlAccessorType (NONE)
@R ("function (kmeans, dataset, distance, k, dimension, subset) kmeans (dataset, distance, k, dimension, subset)")
public class KMeans <K, V> extends ClusteringAdapter<K, V> {

  /**
   * Number of clusters k parameter
   */
  private @Parameter int k;

  /**
   * Collection of clusters result
   */
  private @Result Collection<Collection<K>> clusters;

  @PUT
  @Path ("/k")
  @XmlElement (name = "k")
  public void set (int k) {
    this.k = k;
  }

  @GET
  @Path ("/k")
  public int k () {
    return k;
  }

  @GET
  @Path ("/clusters")
  public Collection<Collection<K>> clusters () {
    return clusters;
  }
}
