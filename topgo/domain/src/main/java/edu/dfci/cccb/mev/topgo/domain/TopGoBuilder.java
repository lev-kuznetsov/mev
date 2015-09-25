package edu.dfci.cccb.mev.topgo.domain;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;

@R (value = "function (dataset, genelist, species, goType, testType, pAdjust, nodeSize, bg) {\n"
            + "if (species == 'human') {\n"
            + "  anno.db = 'org.Hs.eg.db';\n"
            + "  bg = as.vector (read.table (file = file.path (cwd, '../topgo/domain/src/main/resources/HSAPIENS_ALL_GENES.txt'))[ , 1 ]);\n"
            + "} else if (species == 'mouse') {\n"
            + "  anno.db = 'org.Mm.eg.db';\n"
            + "  bg = as.vector (read.table (file = file.path (cwd, '../topgo/domain/src/main/resources/MMUSCULUS_ALL_GENES.txt'))[ , 1 ]);\n"
            + "};\n"
            + "allGenes <- unique (c (genelist, bg));\n"
            + "geneStatus <- rep (0, length (allGenes));\n"
            + "names (geneStatus) <- allGenes;\n"
            + "geneStatus[ genelist ] <- 1;\n"
            + "geneStatus <- factor (geneStatus);\n"

            + "goData <- new ('topGOdata',\n"
            + "               ontology = goType,\n"
            + "               description = 'topGO analysis',\n"
            + "               nodeSize = nodeSize,\n"
            + "               annot = topGO::annFUN.org,\n"
            + "               mapping = anno.db,\n"
            + "               ID = 'symbol',\n"
            + "               allGenes = geneStatus);\n"

            + "topGo.stat = new ('classicCount',\n"
            + "                  testStatistic = if (testType == 'fisher') topGO::GOFisherTest\n"
            + "                                  else topGO::GOKSTest, name = testType);\n"

            + "topGo.result = topGO::getSigGroups (goData, topGo.stat);\n"
            + "topGo.count = capture.output (topGo.result);\n"
            + "totalNodes = as.numeric (unlist (strsplit (topGo.count[ 5 ],' '))[ 1 ]);\n"
            + "topGo.table = topGO::GenTable (goData, topGo.result,\n"
            + "                               topNodes = if (totalNodes > nodeSize) nodeSize\n"
            + "                                          else totalNodes);\n"

            + "colnames (topGo.table) = c ('goId', 'goTerm', 'annotatedGenes',\n"
            + "                            'significantGenes', 'expected', 'pValue');\n"
            + "adj.p = p.adjust (topGo.table[ , 'pValue' ], method = pAdjust);\n"
            + "topGo.table = cbind (topGo.table, adj.p);\n"
            + "lapply (1:dim (topGo.table)[ 1 ], function (x) as.list (topGo.table[ x, ]));\n" +
            "}",
    synchronize = true)
@Accessors (fluent = true, chain = true)
public class TopGoBuilder extends AbstractDispatchedRAnalysisBuilder<TopGoBuilder, TopGo> {

  private @Getter @Setter @Parameter Collection<String> genelist;
  private @Getter @Setter @Parameter String species;
  private @Getter @Setter @Parameter String goType;
  private @Getter @Setter @Parameter String testType;
  private @Getter @Setter @Parameter String pAdjust;
  private @Getter @Setter @Parameter int nodeSize;

  private volatile @Getter @Result TopGo result;

  @Callback
  private void setName () {
    
    if (result != null){      
      result.name (name ());
      result.type (type());
    }
    
  }

  public TopGoBuilder () {
    super ("TopGO Analysis");
  }
}
