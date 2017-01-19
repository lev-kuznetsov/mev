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
package edu.dfci.cccb.mev.analysis.r.kmeans;

import static javax.persistence.CascadeType.ALL;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.analysis.Define;
import edu.dfci.cccb.mev.analysis.Resolve;
import edu.dfci.cccb.mev.analysis.r.Adapter;
import edu.dfci.cccb.mev.analysis.r.Rscript;
import edu.dfci.cccb.mev.dataset.Dataset;
import io.fabric8.annotations.Path;

/**
 * K-means clustering analysis
 * 
 * @author levk
 */
@Entity
@Rscript ("/kmeans.R")
public class Kmeans extends Adapter {
  /**
   * Dataset
   */
  private @Define @OneToOne (cascade = ALL) Dataset dataset;
  /**
   * Number of clusters
   */
  private @Define @Column @Basic int k;
  /**
   * Dimension
   */
  private @Define @Column @Basic String dimension = "column";
  /**
   * Distance metric
   */
  private @Define @Column @Basic String metric = "eu";
  /**
   * Result
   */
  private @Resolve @OneToMany (cascade = ALL) List <Cluster> result;

  /**
   * @param dataset
   */
  @PUT
  @Path ("dataset")
  @JsonProperty (required = false)
  public void dataset (Dataset dataset) {
    this.dataset = dataset;
  }

  /**
   * @param k
   */
  @PUT
  @Path ("k")
  @JsonProperty (required = false)
  public void k (int k) {
    this.k = k;
  }

  /**
   * @param dimension
   */
  @PUT
  @Path ("dimension")
  @JsonProperty (required = false)
  public void dimension (String dimension) {
    this.dimension = dimension;
  }

  /**
   * @param metric
   */
  @PUT
  @Path ("metric")
  @JsonProperty (required = false)
  public void metric (String metric) {
    this.metric = metric;
  }

  /**
   * @return clusters
   */
  @GET
  @Path ("result")
  @JsonIgnore
  public List <Cluster> result () {
    return result;
  }
}
