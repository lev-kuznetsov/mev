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
package edu.dfci.cccb.mev.analysis.r.pathwayenrich;

import static javax.persistence.CascadeType.ALL;

import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.analysis.Define;
import edu.dfci.cccb.mev.analysis.r.Adapter;
import edu.dfci.cccb.mev.analysis.r.Rscript;

/**
 * Pathway enrichment analysis
 * 
 * @author levk
 */
@Entity
@Rscript ("/pathway-enrichment.R")
public class PathwayEnrichment extends Adapter {
  /**
   * Min GS size
   */
  private @Define @Column @Basic int minGsSize = 20;
  /**
   * p-value cutoff
   */
  private @Define ("pvalueCutoff") @Column @Basic double pValueCutoff = 0.25;
  /**
   * p adjustment method
   */
  private @Define @Column @Basic String pAdjustMethod = "fdr";
  /**
   * Organism
   */
  private @Define @Column @Basic String organism = "human";
  /**
   * Gene list
   */
  private @Define @ElementCollection List <String> genelist;
  /**
   * Result
   */
  private @OneToMany (cascade = ALL) Map <String, PathwayEnrichmentEntry> result;

  /**
   * @param minGsSize
   */
  @PUT
  @Path ("minGsSize")
  @JsonProperty (required = false)
  public void minGsSize (int minGsSize) {
    this.minGsSize = minGsSize;
  }

  /**
   * @param pValueCutoff
   */
  @PUT
  @Path ("pValueCutoff")
  @JsonProperty (required = false)
  public void pValueCutoff (double pValueCutoff) {
    this.pValueCutoff = pValueCutoff;
  }

  /**
   * @param pAdjustMethod
   */
  @PUT
  @Path ("pAdjustMethod")
  @JsonProperty (required = false)
  public void pAdjustMethod (String pAdjustMethod) {
    this.pAdjustMethod = pAdjustMethod;
  }

  /**
   * @param organism
   */
  @PUT
  @Path ("organism")
  @JsonProperty (required = false)
  public void organism (String organism) {
    this.organism = organism;
  }

  /**
   * @param genelist
   */
  @PUT
  @Path ("genelist")
  @JsonProperty (required = false)
  public void genelist (List <String> genelist) {
    this.genelist = genelist;
  }

  /**
   * @return result
   */
  @GET
  @Path ("result")
  @JsonIgnore
  public Map <String, PathwayEnrichmentEntry> result () {
    return result;
  }
}
