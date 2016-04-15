package edu.dfci.cccb.mev.voom.domain;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;

@R ("function (dataset, control, experiment) shim ('limma', 'edgeR', callback = function (contrasts.fit, voom, DGEList, lmFit, eBayes, makeContrasts, topTable, calcNormFactors) {"
    + "CurrMtx <- data.matrix (dataset);\n"
    + "dge <- DGEList (counts = CurrMtx);\n"
    + "Tissue <- array (NA, dim=c (1,length (colnames (CurrMtx))));\n"
    + "Tissue[ 1:length (experiment) ] <- 'Experiment';\n"
    + "Tissue[ (length (experiment) + 1):length (colnames (CurrMtx)) ] <- 'Control';\n"
    + "Tissue <- factor (Tissue, levels = c ('Experiment','Control'));\n"
    + "design <- model.matrix (~0 + Tissue);\n"
    + "colnames (design) <- c ('Experiment', 'Control');\n"
    + "v <- voom (CurrMtx, design, plot=FALSE);\n"
    + "fit <- lmFit (v, design);\n"
    + "efit <- eBayes (fit);\n"
    + "contrast.matrix <- makeContrasts (ExpvsContr = Experiment-Control, levels = design);\n"
    + "fit <- eBayes (contrasts.fit (fit, contrast.matrix[ , 'ExpvsContr' ]));\n"
    + "topTable (fit, adjust = 'fdr', number = dim (CurrMtx)[ 1 ])" +
    "}, binder = binder ())")
@Accessors (fluent = true, chain = true)
public class VoomBuilder extends AbstractDispatchedRAnalysisBuilder<VoomBuilder, Voom> {

  private @Getter @Setter @Parameter Collection<String> control;
  private @Getter @Setter @Parameter Collection<String> experiment;
  private @Result Voom result;

  public VoomBuilder () {
    super ("voom");
  }

  @Override
  protected Voom result () {
    return result;
  }

  @Callback
  private void setName () {
    if (result != null)
      result.name (name ());
  }
}
