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
package edu.dfci.cccb.mev.kmeans.domain.r;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.kmeans.domain.contract.Cluster;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeans;
import edu.dfci.cccb.mev.kmeans.domain.contract.KMeansBuilder;
import edu.dfci.cccb.mev.kmeans.domain.prototype.AbstractKMeans;

/**
 * @author levk
 * 
 */
@Accessors (fluent = true)
@R ("function (dataset, k, dimension, metric) {\n"
    + "group <- function (clustering) {\n"
    + "  clusters <- list ();\n"
    + "  for (index in 1:length (clustering)) {\n"
    + "    cluster <- NULL;\n"
    + "    for (entry in names (clustering)) if (clustering[[ entry ]] == index) cluster <- c (cluster, entry);\n"
    + "    clusters[[ index ]] <- cluster;\n"
    + "  }\n"
    + "  clusters;\n"
    + "};\n"
    + "group (cluster::pam (stats::dist (if (dimension == 'column') t (dataset) else dataset, method = metric),"
    + "                     k, diss = TRUE, cluster.only = TRUE));\n" +
    "}")
public class DispatchedRKMeansBuilder extends AbstractDispatchedRAnalysisBuilder<KMeansBuilder, KMeans> implements KMeansBuilder {

  private @Getter @Setter @Parameter @JsonProperty int k;
  private @Parameter String dimension = "column";
  private @Parameter String metric = "eu";

  private @Result Set<Cluster> clusters;

  public DispatchedRKMeansBuilder () {
    super ("K-means Clustering");
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.kmeans.domain.contract.KMeansBuilder#dimension(edu.dfci
   * .cccb.mev.dataset.domain.contract.Dimension) */
  @Override
  public KMeansBuilder dimension (Dimension dimension) {
    this.dimension = dimension.type ().name ().toLowerCase ();
    return this;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.kmeans.domain.contract.KMeansBuilder#metric(edu.dfci
   * .cccb.mev.kmeans.domain.hadoop.Metric) */
  @Override
  public KMeansBuilder metric (String metric) {
    this.metric = metric.toLowerCase ();
    return this;
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder#
   * result() */
  @Override
  @SneakyThrows (InvalidDimensionTypeException.class)
  protected KMeans result () {
    return new AbstractKMeans () {}.clusters ((Set<Cluster>) clusters)
                                   .name (name ())
                                   .type (type ())
                                   .dataset (dataset ())
                                   .dimension (dataset ().dimension (Type.valueOf (dimension.toUpperCase ())));
  }
}
