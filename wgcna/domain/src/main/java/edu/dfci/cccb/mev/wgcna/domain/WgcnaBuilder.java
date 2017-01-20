package edu.dfci.cccb.mev.wgcna.domain;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import lombok.Setter;

import java.util.List;

/**
 * Created by antony on 5/27/16.
 */
@R("function(dataset, distMethod=\"euclidean\", WeightFilter=0.9, sizeLimit=1000, log=TRUE){\n" +
        "  library(WGCNA)\n" +
        "  if(dim(dataset)[1]>sizeLimit){stop(\"WGCNA::Expression Matrix exceeds limit\")}\n" +
        "  datExp=data.frame(t(dataset))\n" +
        "  adjacencyMatrix=WGCNA::adjacency(datExp, distFnc = \"dist\", distOptions = paste0(\"method = '\",distMethod,\"'\"))\n" +
        "  # VisANT output generate four columns  \n" +
        "  # from            to direction method    weight\n" +
        "  VisANT=WGCNA::exportNetworkToVisANT(adjacencyMatrix)\n" +
        "  fVisANT=subset(VisANT, weight>WeightFilter)\n" +
        "  return(fVisANT)\n" +
        "}")
public class WgcnaBuilder extends AbstractDispatchedRAnalysisBuilder<WgcnaBuilder, Wgcna> {

    private Wgcna.Parameters params;
    private @Parameter String distMethod = "euclidean";
    private @Parameter Double WeightFilter = 0.9;
    private @Parameter Integer sizeLimit = 1000;

    public WgcnaBuilder() {super("wgcna");}

    @Override @Parameter
    protected Dataset dataset(){
        return super.subset(this.params.sampleList(), this.params.geneList());
    }

    private @Result List<Wgcna.Edge> edges;

    public WgcnaBuilder parameters(Wgcna.Parameters params) {
        this.params = params;
        this.name(params.name());
        if(params.distMethod()!=null)
            this.distMethod = params.distMethod();
        if(params.WeightFilter()!=null)
            this.WeightFilter = params.WeightFilter();
        if(params.sizeLimit()!=null)
            this.sizeLimit = params.sizeLimit();
        return this;
    }

    @Override
    protected Wgcna result() {
        return new Wgcna(this.params, new Wgcna.Result(edges));
    }


}
