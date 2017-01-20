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

            + "output<-do.call(rbind, EntrezData);\n"
            + "geneList.lfc=as.numeric(output[,2]);\n"
            + "names(geneList.lfc)=output[,1];\n"
            + "geneList.lfc=sort(geneList.lfc, decreasing=TRUE);\n"

            + "gsea.res<-gsePathway(geneList.lfc, nPerm=nPerm, minGSSize=minGSSize,\n"
            + "                     pvalueCutoff=adjPvalueCutoff,"
            + "                     pAdjustMethod=pAdjustMethod, verbose=FALSE);\n"

            + "gsea.res=summary(gsea.res);"
            + "if (is.list (gsea.res)) gsea.res else stop (paste ('gsePathway invalid return value ', jsonlite::toJSON (as.list (gsea.res))));" +
            "}", synchronize = true)
@Accessors (fluent = true, chain = true)
public class GseaBuilder extends AbstractDispatchedRAnalysisBuilder<GseaBuilder, Gsea> {

  private @Parameter @Setter List<LimmaEntry> limma;
  private @Parameter @Setter int nPerm = 100;
  private @Parameter @Setter int minGSSize = 20;
  private @Parameter @Setter String organism = "human";
  private @Parameter @Setter String pAdjustMethod = "fdr";
  private @Parameter("adjPvalueCutoff") @Setter double adjValueCutoff = 0.05;

  private @Getter Gsea result;
  @Result List<GseaEntry> response;
  
  public GseaBuilder () {
    super ("gsea");
  }

  @Callback (CallbackType.SUCCESS)
  private void inject () {
    this.result = new Gsea (name (), response);
  }
}
