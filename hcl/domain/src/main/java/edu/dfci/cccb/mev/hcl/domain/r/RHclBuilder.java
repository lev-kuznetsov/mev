package edu.dfci.cccb.mev.hcl.domain.r;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback.CallbackType;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Error;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.dataset.domain.subset.DataSubset;
import edu.dfci.cccb.mev.hcl.domain.contract.Hcl;
import edu.dfci.cccb.mev.hcl.domain.contract.HclBuilder;
import edu.dfci.cccb.mev.hcl.domain.prototype.AbstractHcl.CHclResult;
import edu.dfci.cccb.mev.hcl.domain.simple.SimpleHcl;

@R ("function (dataset, metric, linkage, dimension) {"
    + "library(amap);\n"
    + "hc2n <- function (hc, flat = FALSE) {\n"
    + "  dist <- 0;\n"
    + "  if (is.null (hc$labels)) labels <- seq(along = hc$order) else labels <- hc$labels;\n"
    + "  putparenthesis <- function (i) {\n"
    + "    j <- hc$merge[i, 1];\n"
    + "    k <- hc$merge[i, 2];\n"
    + "    if (j < 0) {\n"
    + "      left <- labels[-j];\n"
    + "      if (k > 0) dist <- hc$height[i] - hc$height[k] else dist <- hc$height[i];\n"
    + "    } else left <- putparenthesis(j);\n"
    + "    if (k < 0) {\n"
    + "      right <- labels[-k];\n"
    + "      if (j > 0) dist <- hc$height[i] - hc$height[j] else dist <- hc$height[i];\n"
    + "    } else right <- putparenthesis(k);\n"
    + "    if (flat) return (paste (\"(\", left, \":\", dist/2, \",\", right, \":\", dist/2, \")\", sep = \"\"))\n"
    + "    else return (list(left = left, right = right, dist = dist));\n"
    + "  }\n"
    + "  n <- putparenthesis (nrow (hc$merge));\n"
    + "  if (flat) n <- paste(n, \";\", sep = \"\");\n"
    + "  return (n);\n"
    + "};\n"
    + "l <- function (n)"
    + "  if (typeof (n) == 'character') list (name = n) "
    + "  else list (distance = n$dist, left = l (n$left), right = l (n$right));\n"
    + "if(is.null(dimension) || length(dimension)==0) dimension = list(\"row\", \"column\")\n"      
    + "runHcl <- function(dimension){"
    + "  d <- if (dimension == 'row') dataset else t (dataset);\n"
    + "  l (hc2n (stats::hclust (amap::Dist(d, m=metric), method = linkage)));\n"
    + "};\n"
    + "result<-list();\n"
    + "for (dim in dimension) result[[dim]] <- runHcl(dim);\n" 
    + "result;\n" +
    "}")
@Accessors (fluent = true, chain = true)
public class RHclBuilder extends AbstractDispatchedRAnalysisBuilder<HclBuilder, Hcl> implements HclBuilder {

  public RHclBuilder () {
    super ("Hierarchical Clustering");
  }

  private List<Dimension> dimensions = new ArrayList<Dimension>();
  @Override
  public HclBuilder dimension(Dimension dimension){
	  dimensions.add(dimension);
	  return this;
  }
  private @Getter @Setter @Parameter String metric;
  private @Getter @Setter @Parameter String linkage;
  private @Result CHclResult response;
  private @Getter Hcl result;
  private @Getter @Error String error;

  private @Getter @Setter List<String> columns;
  private @Getter @Setter List<String> rows;
  
  private Object subsetlock = new Object();
  private Dataset dataset;
  
  @Parameter("dimension")  
  private List<String> dimensionNames(){
	  List<String> dimensionNames = new ArrayList<String>();		  
	  for(Dimension dim : this.dimensions)
		  dimensionNames.add(dim.type().name().toLowerCase());
	  return dimensionNames;
  }
  
  @Override
  @Parameter
  @SneakyThrows({InvalidDatasetNameException.class, InvalidDimensionTypeException.class})
  protected Dataset dataset (){	  
	  if(columns==null && rows==null)
		  return super.dataset();
	  else{		  
		  synchronized (subsetlock) {				
			  if(this.dataset==null)
				  this.dataset = new DataSubset(super.dataset(), 
						  this.columns() != null ? this.columns() : super.dataset().dimension(Type.COLUMN).keys(),
						  this.rows() != null ? this.rows() : super.dataset().dimension(Type.ROW).keys()
				  );
			  return this.dataset;
		  }		 
	  }
  }
  
  @Callback (CallbackType.SUCCESS)
  private void formatResult () {
    result = new SimpleHcl ().dataset (dataset ()).dimensions (dimensions).name (name ()).type (type ()).result (response);
  }
}
