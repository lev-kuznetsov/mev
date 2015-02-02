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

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.RAnalysisAdapter;

/**
 * Common clustering API
 * 
 * @author levk
 * @since CRYSTAL
 */
@XmlRootElement
@XmlAccessorType (NONE)
public abstract class ClusteringAnalysisAdapter <K, V> extends RAnalysisAdapter<K, V> {

  /**
   * Distance metric
   */
  private @Parameter Distance distance = DEFAULT;

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
}
