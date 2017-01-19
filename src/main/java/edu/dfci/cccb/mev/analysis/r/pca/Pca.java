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
package edu.dfci.cccb.mev.analysis.r.pca;

import static javax.persistence.CascadeType.ALL;

import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
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
 * PCA analysis
 * 
 * @author levk
 */
@Entity
@Rscript ("/pca.R")
public class Pca extends Adapter {
  /**
   * Dataset
   */
  private @Define @OneToOne (cascade = ALL) Dataset dataset;
  /**
   * Rows
   */
  private @Define @ElementCollection List <String> rows;
  /**
   * Columns
   */
  private @Define @ElementCollection List <String> columns;
  /**
   * X
   */
  private @Resolve @OneToMany (cascade = ALL) Map <String, X> x;
  /**
   * Standard deviations
   */
  private @Resolve @ElementCollection List <Double> sdev;

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
   * @param rows
   */
  @PUT
  @Path ("rows")
  @JsonProperty (required = false)
  public void rows (List <String> rows) {
    this.rows = rows;
  }

  /**
   * @param columns
   */
  @PUT
  @Path ("columns")
  @JsonProperty (required = false)
  public void columns (List <String> columns) {
    this.columns = columns;
  }

  /**
   * @return x
   */
  @GET
  @Path ("x")
  @JsonIgnore
  public Map <String, X> x () {
    return x;
  }

  /**
   * @return sdev
   */
  @GET
  @Path ("sdev")
  @JsonIgnore
  public List <Double> sdev () {
    return sdev;
  }
}
