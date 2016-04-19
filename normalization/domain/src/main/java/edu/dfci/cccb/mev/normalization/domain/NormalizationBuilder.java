package edu.dfci.cccb.mev.normalization.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.dfci.cccb.mev.dataset.domain.contract.*;
import lombok.*;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.*;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback.CallbackType;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.normalization.domain.Normalization.NormalizationParameters;

@R ("function(dataset,method='deseq'){\n"
        + "phenoData=NULL;featureData=NULL;log = TRUE;\n"
    + "library('rafalib')\n"
    + "library('metagenomeSeq')\n"
    + "library('DESeq')\n"
    + "library('edgeR')\n"
    + "library('DESeq2')\n"
    + "library('matrixStats')\n"

    + "counts <- data.matrix (dataset)\n"
    + "obj = newMRexperiment(dataset,phenoData,featureData)\n"
    + "obj = cumNorm(obj)\n"

    + "METHODS <- c('css','css2','tss', 'deseq', 'tmm','upperquantile','vst','pa')\n"
    + "method <- pmatch(tolower(method), METHODS)\n"
    + "if (is.na(method)) stop('invalid normalization method')\n"
    + "if (method == -1) stop('ambiguous transformation method')\n"

    + "if(method == '1') nmat = MRcounts(obj,log=FALSE,norm=TRUE,sl=median(normFactors(obj)))\n"

    + "counts = MRcounts(obj,norm=FALSE,log=FALSE)\n"
    + "normNewCSS <-function(obj,p){\n"
    + "  normFactor = unlist(metagenomeSeq::calcNormFactors(obj, p = p))\n"
    + "  sweep(obj,2,normFactor/median(normFactor),'/')\n"
    + "}\n"

    + "if(method == '2'){\n"
    + "  css2Normalization<-function(mat,rel=.1){\n"
    + "    smat = sapply(1:ncol(mat),function(i){sort(mat[,i],decreasing=FALSE)})\n"
    + "    ref  = rowMeans(smat)\n"
    + "    yy = mat\n"
    + "    yy[yy==0]=NA\n"
    + "    ncols = ncol(mat)\n"
    + "    refS = sort(ref)\n"
    + "    k = which(refS>0)[1]\n"
    + "    lo = (length(refS)-k+1)\n"
    + "    diffr = sapply(1:ncols,function(i){\n"
    + "      refS[k:length(refS)] - quantile(yy[,i],p=seq(0,1,length.out=lo),na.rm=TRUE)\n"
    + "    })\n"
    + "    q = sapply(1:ncol(diffr),function(i){\n"
    + "      col = diffr[,i]\n"
    + "      which(abs(diff(col))/col[-1]>rel)[1] / length(col)\n"
    + "    })\n"
    + "    q[is.na(q)] = median(q,na.rm=TRUE)\n"
    + "    names(q) = colnames(mat)\n"
    + "    diffr2 = matrixStats::rowMedians(abs(diffr),na.rm=TRUE)\n"
    + "    x = which(abs(diff(diffr2))/diffr2[-1]>rel)[1] / length(diffr2)\n"
    + "    if(x<=0.50) x = 0.50\n"
    + "    typicalCSS = x\n"
    + "    newCSS = q\n"
    + "    unlist(newCSS)\n"
    + "  }\n"
    + "  p = css2Normalization(counts)\n"
    + "  nmat = normNewCSS(counts,p)\n"
    + "}\n"

    + "if(method == '3'){\n"
    + "  tss = colSums(counts)\n"
    + "  nmat = sweep(counts,2,tss,'/')\n"
    + "}\n"

    + "if(method == '4'){\n"
    + "  deseq_sf = estimateSizeFactorsForMatrix(counts)\n"
    + "  nmat = sweep(counts,2,deseq_sf,'/')\n"
    + "}\n"

    + "if(method == '5'){\n"
    + "  d <- DGEList(counts, lib.size = as.vector(colSums(counts)))\n"
    + "  d <- calcNormFactors(d,method='TMM')\n"
    + "  nmat = cpm(d, normalized.lib.size=TRUE)\n"
    + "}\n"

    + "if(method == '6'){\n"
    + "  y = counts\n"
    + "  y[y==0] = NA\n"
    + "  scale = 1\n"
    + "  normalizationScale = colQuantiles(y,p=.75,na.rm=TRUE)\n"
    + "  nmat = sweep(counts,2,normalizationScale/scale,'/')\n"
    + "}\n"

    + "if(log && exists('nmat')) nmat = log2(nmat+1)\n"

    + "if(method == '7'){\n"
    + "  dds <- DESeqDataSetFromMatrix(counts, pData(obj), ~1)\n"
    + "  dds <- estimateSizeFactors(dds)\n"
    + "  dds <- estimateDispersions(dds)\n"
    + "  nmat = getVarianceStabilizedData(dds)\n"
    + "}\n"

    + "if(method == '8') nmat = 1-(1-counts>0)\n"

    + "write.table(nd,file='out.tsv',sep='\t',quote=FALSE,col.names=NA)\n"
    + "return(NULL)\n" +
    "}")
@Accessors (fluent = true, chain = true)
public class NormalizationBuilder extends AbstractDispatchedRAnalysisBuilder<NormalizationBuilder, Normalization> {

  private @Parameter String method = "deseq";
  private @Result Dataset normalized;
  private Normalization.NormalizationParameters params;
  public NormalizationBuilder params(NormalizationParameters params){
    this.params = params;
    this.method = params.method();
    name(params.name());
    return this;
  }

  public NormalizationBuilder () {
    super ("normalization");
  }

  @Callback (CallbackType.SUCCESS)
  @SneakyThrows({InvalidDimensionTypeException.class})
  private void export ()  {
    Dimension columns = dataset().dimension(COLUMN).subset(null);
    normalized.set(columns);
    Dimension rows = dataset().dimension(ROW).subset(null);
    normalized.set(rows);
  }

  @Override
  protected Normalization result () {
    return new Normalization (params, normalized);
  }

}
