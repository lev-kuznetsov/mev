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
package edu.dfci.cccb.mev.analysis.r.edge;

import static javax.persistence.CascadeType.ALL;

import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
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
import edu.dfci.cccb.mev.analysis.r.R;
import edu.dfci.cccb.mev.dataset.Dataset;
import io.fabric8.annotations.Path;

/**
 * @author levk
 */
@Entity
@R ("library ('edgeR');\n" + "dataset <- as.data.frame (dataset);\n" + "samples <- colnames (dataset);\n"
    + "if (!(experiment %in% samples) || !(control %in% samples)) {\n"
    + "  stop ('Could not find samples corresponding to the desired contrast');\n" + "}\n"
    + "CurrMtx <- dataset[, c (experiment, control)];\n"
    + "conditions <- array (NA, dim = c (1, length (colnames (CurrMtx))));\n"
    + "conditions[1:length (experiment)] = 'Experiment';\n"
    + "conditions[(length (experiment) + 1):length (colnames (CurrMtx))] = 'Control';\n" +

    "cds <- DGEList (counts = dataset, group = conditions);\n" +

    "cds <- estimateCommonDisp (cds);\n"
    + "de <- exactTest (cds, dispersion = 'common', pair = c ('Experiment', 'Control'));\n"
    + "res <- topTags (de, n = dim (de$table)[1], adjust.method = method);\n"
    + "result <- lapply (as.data.frame (t (res.table)),"
    + "                  function (e, n) setNames (list (logFc = e[1], logCpm = e[2], pValue = e[3], fdr = e[4]), n),"
    + "                  c ('logFc', 'logCpm', 'pValue', sapply (colnames (result), tolower)[4]))")
public class Edge extends Adapter {
  /**
   * Dataset
   */
  private @Define @OneToOne (cascade = ALL) Dataset dataset;
  /**
   * Experiment sample set
   */
  private @Define @ElementCollection List <String> experiment;
  /**
   * Control sample set
   */
  private @Define @ElementCollection List <String> control;
  /**
   * Method
   */
  private @Define @Column @Basic String method = "fdr";
  /**
   * Result
   */
  private @Resolve @OneToMany (cascade = ALL) Map <String, EdgeEntry> result;

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
   * @param experiment
   */
  @PUT
  @Path ("experiment")
  @JsonProperty (required = false)
  public void experiment (List <String> experiment) {
    this.experiment = experiment;
  }

  /**
   * @param control
   */
  @PUT
  @Path ("control")
  @JsonProperty (required = false)
  public void control (List <String> control) {
    this.control = control;
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
   * @return result
   */
  @GET
  @Path ("result")
  @JsonIgnore
  public Map <String, EdgeEntry> result () {
    return result;
  }
}
