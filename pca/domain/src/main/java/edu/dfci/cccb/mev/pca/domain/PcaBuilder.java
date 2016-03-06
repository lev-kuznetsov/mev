package edu.dfci.cccb.mev.pca.domain;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;
import edu.dfci.cccb.mev.dataset.domain.subset.DataSubset;

@R ("function (dataset, top) {\n"
    + "pca <- prcomp (t (data.matrix (na.omit(dataset))));\n"
    + "list (sdev = pca$sdev,"
//    + "      center = as.list (pca$center),"
//    + "      scale = pca$scale,"
    + "      x = apply (as.data.frame (t (pca$x)), 2, function (x) as.list (x)[ 1 : top ])"
//    + "      rotation = apply (as.data.frame (t (pca$rotation)), 2, function (x) as.list (x))"
    + ");" +
    "}")
@Accessors(fluent=true)
public class PcaBuilder extends AbstractDispatchedRAnalysisBuilder<PcaBuilder, Pca> {

  public PcaBuilder () {
    super ("pca");
  }
  @Override
  @Parameter
  @SneakyThrows({InvalidDatasetNameException.class, InvalidDimensionTypeException.class})
  protected Dataset dataset (){	  
	  if(sampleList==null && geneList==null)
		  return super.dataset();
	  else{		  
		  
		  return new DataSubset(super.dataset(), 
				 this.sampleList() != null ? this.sampleList() : super.dataset().dimension(Type.COLUMN).keys(),
				 this.geneList() != null ? this.geneList() : super.dataset().dimension(Type.ROW).keys());
	  }
  }
  
  private @Result Pca result;

  private @Setter @Getter @Parameter int top = 3;
  private @Getter @Setter List<String> sampleList;
  private @Getter @Setter List<String> geneList;
  
  @Override
  protected Pca result () {
    return result;
  }

  @Callback
  private void setName () {
    if (result != null){      
      result.name (name ());
      result.type (type());
    }
  }
}
