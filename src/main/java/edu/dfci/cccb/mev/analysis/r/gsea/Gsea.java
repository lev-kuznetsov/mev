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
package edu.dfci.cccb.mev.analysis.r.gsea;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.analysis.Define;
import edu.dfci.cccb.mev.analysis.Resolve;
import edu.dfci.cccb.mev.analysis.r.R;
import edu.dfci.cccb.mev.analysis.r.R.Adapter;
import edu.dfci.cccb.mev.analysis.r.limma.LimmaEntry;
import io.fabric8.annotations.Path;

/**
 * @author levk
 */
@Entity
@JsonInclude (NON_EMPTY)
@R ("dataset <- to.data.frame (dataset);\n" + "library (org.Hs.eg.db);\n" + "library (ReactomnePA);\n"
    + "GStoEG_map <- as.list (org.Hs.eg.ALIAS2EG);\n" + "absMax <- function (x) x[which.max (abs (x))];\n"
    + "CurrData <- na.omit (limma);\n" +

    "EntrezData <- lapply (limma[, 'SYMBOL'], function (GS) {\n"
    + "  list (EntrezID = GStoEG_map[[GS]][1], logFC = absMax (limma[limma[, 'SYMBOL]==GS, 'logFC'])))\n" + "});\n"
    + "output <- do.call (rbind, EntrezData);\n" + "geneList.lfc = as.numeric (output[, 2]);\n"
    + "names (geneList.lfc) = output[, 1];\n" + "geneList.lfc = sort (geneList.lfc, decreasing = TRUE);\n" +

    "gsea.res <- gsePathway (geneList.lfc, nPerm = nPerm, minGSSize = minGSSize, pvalueCutoff = adjPValueCutoff,"
    + "                      pAdjustMethod = pAdjustMethod, verbose = FALSE);\n" + "result <- summary (gsea.res);")
// FIXME organism used to be a parameter but it wasn't used in the script, I
// think the script assumes human
public class Gsea extends Adapter {
  /**
   * Identifier
   */
  private @Id @GeneratedValue (strategy = AUTO) long id;
  /**
   * Limma result
   */
  private @Define @OneToMany (cascade = ALL) Map <String, LimmaEntry> limma;
  /**
   * Permutations
   */
  private @Define @Column @Basic int nPerm = 100;
  /**
   * Minimum GS size
   */
  private @Define @Column @Basic int minGSSize = 20;
  /**
   * Adjust cutoff
   */
  private @Define @Column @Basic double adjPValueCutoff = 0.05;
  /**
   * Adjust method
   */
  private @Define @Column @Basic String pAdjustMethod = "fdr";
  /**
   * Result
   */
  private @Resolve @OneToMany (cascade = ALL) Map <String, GseaEntry> result;

  /**
   * @param limma
   */
  @PUT
  @Path ("limma")
  @JsonProperty (required = false)
  public void limma (Map <String, LimmaEntry> limma) {
    this.limma = limma;
  }

  /**
   * @param nPerm
   */
  @PUT
  @Path ("nPerm")
  @JsonProperty (required = false)
  public void nPerm (int nPerm) {
    this.nPerm = nPerm;
  }

  /**
   * @param minGSSize
   */
  @PUT
  @Path ("minGSSize")
  @JsonProperty (required = false)
  public void minGSSize (int minGSSize) {
    this.minGSSize = minGSSize;
  }

  /**
   * @param adjPValueCutoff
   */
  @PUT
  @Path ("adjPValueCutoff")
  @JsonProperty (required = false)
  public void adjPValueCutoff (double adjPValueCutoff) {
    this.adjPValueCutoff = adjPValueCutoff;
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

  @GET
  @Path ("result")
  @JsonIgnore
  public Map <String, GseaEntry> result () {
    return result;
  }
}
