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
package edu.dfci.cccb.mev.analysis.r.limma;

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
import edu.dfci.cccb.mev.analysis.r.R;
import edu.dfci.cccb.mev.dataset.Dataset;
import io.fabric8.annotations.Path;

/**
 * Limma analysis
 * 
 * @author levk
 */
@Entity
@R ("library(limma);\n" + "dataset <- as.data.frame (dataset);\n"
    + "run.limma<-function(in.mtx, Experiment=NA, Control=NA){\n" + "  CurrMtx=in.mtx;\n"
    + "  CurrMtx=in.mtx[,Experiment];\n" + "  CurrMtx=cbind(CurrMtx, in.mtx[,Control]);\n"
    + "  Tissue=array(NA, dim=c(1,length(colnames(CurrMtx))));\n" + "  Tissue[1:length(Experiment)]='Experiment';\n"
    + "  Tissue[(length(Experiment)+1):length(colnames(CurrMtx))]='Control';\n"
    + "  Tissue=factor(Tissue,levels=c('Experiment','Control'));\n" + "  design=model.matrix(~0+Tissue);\n"
    + "  colnames(design)=c('Experiment', 'Control');\n" + "  fit<-lmFit(CurrMtx, design);\n" + "  efit<-eBayes(fit);\n"
    + "  contrast.matrix=makeContrasts(ExpvsContr=Experiment-Control, levels=design);\n"
    + "  fit<-eBayes(contrasts.fit(fit, contrast.matrix[,'ExpvsContr']));\n"
    + "  result_full=topTable(fit, adjust='fdr', number=dim(CurrMtx)[1]);\n" + "  return(result_full);\n" + "};\n"
    + "in.mtx=dataset;\n"

    // Check to determine if matrix contains negative values
    // off set the matrix to operate in positive values for limma
    // starting with 0
    + "min.val=min(in.mtx,na.rm=TRUE);\n" + "in.mtx=if(min.val<0){in.mtx+min.val*-1}else{in.mtx};\n"
    // Assign group
    + "EXP=experiment;\n" + "CON=control;\n"
    // Run limma
    + "result=run.limma(in.mtx, Experiment=EXP, Control=CON);\n"
    // Condition to add the ID column for limma version AFTER R 3.2.1, which
    // return 6 column without the ID column . Updated 09/28/2015
    + "if(dim(result)[2]==6){ ID=rownames(result); result=cbind(ID, result); };\n"

    // reassign colnames
    + "colnames(result)=c('ID', 'Log Fold Change', 'Average Expression', 't', 'P-value', 'q-value', 'B');\n"

    // If the matrix contains negative values,
    // adjust the offset back to its original input values
    + "result[,'Average Expression']=if(min.val<0){result[,'Average Expression']-(min.val*-1)}else\n"
    + "{result[,'Average Expression']};\n"

    + "unname(apply(result,1,function(x)"
    + "  list(id=unname(x[1]),logFc=as.numeric(unname(x[2])),averageExpression=as.numeric(unname(x[3])),"
    + "       pValue=as.numeric(unname(x[5])),t=as.numeric(unname(x[4])),qValue=as.numeric(unname(x[6])))))")
public class Limma extends Adapter {
  /**
   * Dataset
   */
  private @Define @OneToOne (cascade = ALL) Dataset dataset;
  /**
   * Experiment key set
   */
  private @Define @ElementCollection List <String> experiment;
  /**
   * Control key set
   */
  private @Define @ElementCollection List <String> control;
  /**
   * Result set
   */
  private @Resolve @OneToMany (cascade = ALL) Map <String, LimmaEntry> result;

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
   * @return result
   */
  @GET
  @Path ("result")
  @JsonIgnore
  public Map <String, LimmaEntry> result () {
    return result;
  }
}
