package edu.dfci.cccb.mev.pe.domain;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;

@R (value = "function (geneList, minGSSize, pvalueCutoff, pAdjustMethod, organism='human') {\n"
            + "library (org.Hs.eg.db);\n"
            + "GStoEG_map <- as.list (org.Hs.egALIAS2EG);\n"
            + "require(ReactomePA);\n"
            + "EntrezList = lapply (na.omit (geneList), function (GS, GStoEG_map) GStoEG_map[[GS]][1], GStoEG_map = GStoEG_map);\n"
            + "enrichPways <- enrichPathway (gene = EntrezList, minGSSize = minGSSize, organism = organism,"
            + "                              pvalueCutoff = pvalueCutoff, pAdjustMethod = pAdjustMethod, readable = T);\n"
            + "summary (enrichPways);\n" +
            "}",
    synchronize = true)
@Accessors (fluent = true)
public class PathwayEnrichmentBuilder extends AbstractDispatchedRAnalysisBuilder<PathwayEnrichmentBuilder, PathwayEnrichment> {

  private @Getter @Setter @Parameter int minGSSize = 20;
  private @Getter @Setter @Parameter double pvalueCutoff = 0.25;
  private @Getter @Setter @Parameter String pAdjustMethod = "fdr";
  private @Getter @Setter @Parameter String organism = "human";
  private @Getter @Setter @Parameter ("geneList") Collection<String> genelist;
  private @Getter @Setter @Result PathwayEnrichment result;

  public PathwayEnrichmentBuilder () {
    super ("pe");
  }
}
