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

package edu.dfci.cccb.mev.hclust.domain;

import static javax.xml.bind.annotation.XmlAccessType.NONE;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Result;
import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.annotation.Analysis;
import edu.dfci.cccb.mev.dataset.domain.r.RAnalysisAdapter;
import edu.dfci.cccb.mev.hclust.domain.Agglomeration.Average;
import edu.dfci.cccb.mev.hclust.domain.Metric.Euclidean;

/**
 * Hierarchical clustering analysis
 * 
 * @author levk
 * @since ASHA
 */
@Analysis ("hclust")
@XmlRootElement
@XmlAccessorType (NONE)
@R ("function (dataset, agglomeration, metric, dist, hclust, newick)"
    + "newick (hclust (dist (dataset, method = metric), method = agglomeration))")
public class HierarchicalClustering <K, V> extends RAnalysisAdapter {

  private @Parameter @XmlElement (required = false) Agglomeration agglomeration = new Average ();
  private @Parameter @XmlElement (required = false) Metric metric = new Euclidean ();
  private @Parameter @Inject Dataset<K, V> dataset;

  private @Result Node<K> root;

  @PUT
  @Path ("/agglomeration")
  public void set (@QueryParam ("method") Agglomeration agglomeration) {
    if (!agglomeration.equals (this.agglomeration))
      root = null;
    this.agglomeration = agglomeration;
  }

  @GET
  @Path ("/agglomeration")
  public Agglomeration agglomeration () {
    return agglomeration;
  }

  @PUT
  @Path ("/metric")
  public void set (@QueryParam ("method") Metric metric) {
    if (!metric.equals (this.metric))
      root = null;
    this.metric = metric;
  }

  @GET
  @Path ("/metric")
  public Metric metric () {
    return metric;
  }

  @GET
  @Path ("/tree")
  @XmlElement (nillable = true, name = "tree")
  public Node<K> root () {
    return root;
  }
}
