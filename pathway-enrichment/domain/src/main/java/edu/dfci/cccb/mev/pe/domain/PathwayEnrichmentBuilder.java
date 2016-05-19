package edu.dfci.cccb.mev.pe.domain;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.RDispatcher;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback.CallbackType;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.pe.domain.PathwayEnrichment.PathwayEnrichmentParameters;

@R (value = "function (geneList, minGSSize, pvalueCutoff, pAdjustMethod, organism='human') {\n"
            + "library (org.Hs.eg.db);\n"
            + "GStoEG_map <- as.list (org.Hs.egALIAS2EG);\n"
            + "require(ReactomePA);\n"
            + "EntrezList = lapply (na.omit (geneList), function (GS, GStoEG_map) GStoEG_map[[GS]][1], GStoEG_map = GStoEG_map);\n"
            + "enrichPways <- enrichPathway (gene = EntrezList, minGSSize = minGSSize, organism = organism,"
            + "                              pvalueCutoff = pvalueCutoff, pAdjustMethod = pAdjustMethod, readable = T);\n"
            + "sum <- summary (enrichPways);\n"
            + "if (is.list (sum)) sum else stop (paste ('enrichPathway invalid return value ', jsonlite::toJSON (as.list (sum))));" +
         "}",
    synchronize = true)
@Accessors (fluent = true, chain = true)
public class PathwayEnrichmentBuilder extends AbstractDispatchedRAnalysisBuilder<PathwayEnrichmentBuilder, PathwayEnrichment> {

  private @Getter @Setter @Parameter int minGSSize = 20;
  private @Getter @Setter @Parameter double pvalueCutoff = 0.25;
  private @Getter @Setter @Parameter String pAdjustMethod = "fdr";
  private @Getter @Setter @Parameter String organism = "human";
  private @Getter @Setter @Parameter ("geneList") Collection<String> genelist;
  
  private @Result List<PathwayEnrichmentEntry> dtoResult;
  
  private @Getter PathwayEnrichment result;
  private @Setter PathwayEnrichmentParameters params;
  
  public PathwayEnrichmentBuilder () {    
    super ("pe");    
    result = new PathwayEnrichment();
  }

  @Callback(CallbackType.SUCCESS)
  private void onSuccess(){
    result.result (dtoResult);
  }
  @Callback
  private void onCompleted(){
    result.params (params);
  }
}
