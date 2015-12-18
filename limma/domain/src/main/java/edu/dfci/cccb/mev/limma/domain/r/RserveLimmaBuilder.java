package edu.dfci.cccb.mev.limma.domain.r;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback.CallbackType;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.limma.domain.contract.Limma;
import edu.dfci.cccb.mev.limma.domain.contract.LimmaBuilder;
import edu.dfci.cccb.mev.limma.domain.prototype.AbstractLimma;
import edu.dfci.cccb.mev.limma.domain.simple.SimpleEntry;

@Accessors (fluent = true, chain = true)
@R ("function (dataset, control, experiment) {\n"
    + "library(limma);\n"
    + "run.limma<-function(in.mtx, Experiment=NA, Control=NA){\n"

    + "  CurrMtx=in.mtx;\n"
    + "  CurrMtx=in.mtx[,Experiment];\n"

    + "  CurrMtx=cbind(CurrMtx, in.mtx[,Control]);\n"

    + "  Tissue=array(NA, dim=c(1,length(colnames(CurrMtx))));\n"
    + "  Tissue[1:length(Experiment)]='Experiment';\n"
    + "  Tissue[(length(Experiment)+1):length(colnames(CurrMtx))]='Control';\n"

    + "  Tissue=factor(Tissue,levels=c('Experiment','Control'));\n"
    + "  design=model.matrix(~0+Tissue);\n"
    + "  colnames(design)=c('Experiment', 'Control');\n"

    + "  fit<-lmFit(CurrMtx, design);\n"
    + "  efit<-eBayes(fit);\n"

    + "  contrast.matrix=makeContrasts(ExpvsContr=Experiment-Control, levels=design);\n"
    + "  fit<-eBayes(contrasts.fit(fit, contrast.matrix[,'ExpvsContr']));\n"
    + "  result_full=topTable(fit, adjust='fdr', number=dim(CurrMtx)[1]);\n"
    + "  return(result_full);\n"
    + "};\n"

    + "in.mtx=dataset;\n"

    // Check to determine if matrix contains negative values
    // off set the matrix to operate in positive values for limma
    // starting with 0
    + "min.val=min(in.mtx,na.rm=TRUE);\n"
    + "in.mtx=if(min.val<0){in.mtx+min.val*-1}else{in.mtx};\n"

    // Assign group
    + "EXP=experiment;\n"
    + "CON=control;\n"

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
    + "result=result[result$`P-value` <= " + RserveLimmaBuilder.THRESHOLD + ",];\n"
    
    + "unname(apply(result,1,function(x)"
    + "  list(id=unname(x[1]),logFoldChange=as.numeric(unname(x[2])),averageExpression=as.numeric(unname(x[3])),"
    + "       pValue=as.numeric(unname(x[5])),qValue=as.numeric(unname(x[6])))))" +
    "}")
public class RserveLimmaBuilder extends AbstractDispatchedRAnalysisBuilder<LimmaBuilder, Limma> implements LimmaBuilder {

  public static final double THRESHOLD = 0.05; 
  private @Setter Selection control;
  private @Setter Selection experiment;
  private @Getter Limma result;
  private @Result List<SimpleEntry> entries;

  public RserveLimmaBuilder () {
    super ("LIMMA Differential Expression Analysis");
  }

  @Parameter
  private String[] control () {
    return control.keys ().toArray (new String[0]);
  }

  @Parameter
  private String[] experiment () {
    return experiment.keys ().toArray (new String[0]);
  }

  @Callback (CallbackType.SUCCESS)
  private void receive () {
    result = new AbstractLimma () {

      @Override
      public Iterable<? extends Entry> full () {
        return entries;
      }
    }.name (name ()).type (type ());
  }
}
