package edu.dfci.cccb.mev.gsea.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback.CallbackType;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;

@R (value = "function (limma, nPerm, minGSSize, organism, pAdjustMethod, adjPvalueCutoff) {"
            + "library(org.Hs.eg.db);\n"
            + "require(ReactomePA);\n"

            + "GStoEG_map <- as.list(org.Hs.egALIAS2EG);\n"

            + "absMax<-function(X){return(X[which.max( abs(X) )])};\n"

            + "CurrData=na.omit(limma);\n"
            + "EntrezData=lapply(limma[,'SYMBOL'], function(GS, GStoEG_map){\n"
            + "  return(list(EntrezID=GStoEG_map[[GS]][1],"
            + "              logFC=absMax(limma[limma[,'SYMBOL']==GS, 'logFC'])))\n"
            + "}, GStoEG_map=GStoEG_map);\n"

            + "output=matrix(unlist(EntrezData), ncol = 2, byrow = TRUE);\n"
            + "geneList.lfc=as.numeric(output[,2]);\n"
            + "names(geneList.lfc)=output[,1];\n"
            + "geneList.lfc=sort(geneList.lfc, decreasing=TRUE);\n"

            + "gsea.res<-gsePathway(geneList.lfc, nPerm, minGSSize=input$minGSSize,\n"
            + "                     pvalueCutoff=input$adjPvalueCutoff,"
            + "                     pAdjustMethod=input$pAdjustMethod, verbose=FALSE);\n"

            + "gsea.res=summary(gsea.res);" +
            "}", synchronize = false)
@Accessors (fluent = true, chain = true)
public class GseaBuilder extends AbstractDispatchedRAnalysisBuilder<GseaBuilder, Gsea> {

  private @Parameter @Setter List<LimmaEntry> limma;
  private @Parameter @Setter int nPerm = 100;
  private @Parameter @Setter int minGSSize = 20;
  private @Parameter @Setter String organism = "human";
  private @Parameter @Setter String pAdjustMethod = "fdr";
  private @Parameter @Setter double adjValueCutoff = 0.05;

  private @Getter Gsea result;

  public GseaBuilder () {
    super ("gsea");
  }

  @Callback (CallbackType.SUCCESS)
  private void inject (@Result List<GseaEntry> result) {
    this.result = new Gsea (name (), result);
  }
}
