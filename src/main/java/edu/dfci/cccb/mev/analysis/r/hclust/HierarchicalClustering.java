/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Dana-Farber Cancer Institute
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package edu.dfci.cccb.mev.analysis.r.hclust;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static javax.persistence.CascadeType.ALL;

import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.analysis.Define;
import edu.dfci.cccb.mev.analysis.Resolve;
import edu.dfci.cccb.mev.analysis.r.Adapter;
import edu.dfci.cccb.mev.analysis.r.R;
import edu.dfci.cccb.mev.dataset.Dataset;
import io.fabric8.annotations.Path;

/**
 * Hierarchical clustering analysis
 * 
 * @author levk
 */
@Entity
@JsonInclude (NON_EMPTY)
@R ("dataset <- as.data.frame (dataset);\n" + "hc2n <- function (hc, flat = FALSE) {\n" + "  dist <- 0;\n"
    + "  if (is.null (hc$labels)) labels <- seq(along = hc$order) else labels <- hc$labels;\n"
    + "  putparenthesis <- function (i) {\n" + "    j <- hc$merge[i, 1];\n" + "    k <- hc$merge[i, 2];\n"
    + "    if (j < 0) {\n" + "      left <- labels[-j];\n"
    + "      if (k > 0) dist <- hc$height[i] - hc$height[k] else dist <- hc$height[i];\n"
    + "    } else left <- putparenthesis(j);\n" + "    if (k < 0) {\n" + "      right <- labels[-k];\n"
    + "      if (j > 0) dist <- hc$height[i] - hc$height[j] else dist <- hc$height[i];\n"
    + "    } else right <- putparenthesis(k);\n"
    + "    if (flat) return (paste (\"(\", left, \":\", dist/2, \",\", right, \":\", dist/2, \")\", sep = \"\"))\n"
    + "    else return (list(left = left, right = right, dist = dist));\n" + "  }\n"
    + "  n <- putparenthesis (nrow (hc$merge));\n" + "  if (flat) n <- paste(n, \";\", sep = \"\");\n"
    + "  return (n);\n" + "};\n" + "l <- function (n)" + "  if (typeof (n) == 'character') list (name = n) "
    + "  else list (distance = n$dist, children = list (l (n$left), l (n$right)));\n"
    + "if(is.null(dimension) || length(dimension)==0) dimension = list('row', 'column')\n"
    + "runHcl <- function (dimension, filter){" + "  d <- if (dimension == 'row') dataset else t (dataset);\n"
    + "  d <- d[filter,];\n" + "  l (hc2n (stats::hclust (cluster::daisy (d, m = metric), method = linkage)));\n"
    + "};\n" + "for (dim in names (dimensions)) assign (dim, runHcl (dim, dimensions[dim]));")
public class HierarchicalClustering extends Adapter {
  /**
   * Dataset
   */
  private @Define @OneToOne (cascade = ALL) Dataset dataset;
  /**
   * Method
   */
  private @Define @Column @Basic String method = "eu";
  /**
   * Linkage
   */
  private @Define @Column @Basic String linkage = "complete";
  /**
   * Dimension name to dimension key set filter
   */
  private @Define @OneToMany (cascade = ALL) Map <String, Filter> dimensions;
  /**
   * Row tree
   */
  private @Resolve (required = false) @OneToOne (cascade = ALL) Node row;
  /**
   * Column tree
   */
  private @Resolve (required = false) @OneToOne (cascade = ALL) Node column;

  /**
   * @param dataset
   */
  @PUT
  @Path ("dataset")
  @JsonProperty (required = false)
  public void dataset (Dataset dataset) {
    this.dataset = dataset;
    this.dimensions = of (dataset.dimension ("column")).collect (toMap (d -> "column", d -> Filter.filter (d.keys ())));
  }

  /**
   * @param method
   */
  @PUT
  @Path ("method")
  @JsonProperty (required = false)
  public void method (String method) {
    this.method = method;
  }

  /**
   * @param dimensions
   */
  @PUT
  @Path ("dimensions")
  @JsonProperty (required = false)
  public void filter (Map <String, Filter> dimensions) {
    this.dimensions = dimensions;
  }

  /**
   * @param dimension
   */
  @GET
  @Path ("{dimension}")
  @JsonIgnore
  public Node result (@PathParam ("dimension") String dimension) {
    if ("row".equals (dimension)) return row;
    else if ("column".equals (dimension)) return column;
    else throw new BadRequestException ("Unrecognized dimension name " + dimension);
  }
}
