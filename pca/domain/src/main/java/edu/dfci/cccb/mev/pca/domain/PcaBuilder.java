package edu.dfci.cccb.mev.pca.domain;

import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;

@R ("function (dataset) {\n"
    + "pca <- prcomp (t (data.matrix (dataset)));\n"
    + "list (sdev = pca$sdev,"
    + "      center = as.list (pca$center),"
    + "      scale = pca$scale,"
    + "      x = apply (as.data.frame (t (pca$x)), 2, function (x) as.list (x)),"
    + "      rotation = apply (as.data.frame (t (pca$rotation)), 2, function (x) as.list (x)));" +
    "}")
public class PcaBuilder extends AbstractDispatchedRAnalysisBuilder<PcaBuilder, Pca> {

  public PcaBuilder () {
    super ("pca");
  }

  private @Result Pca result;

  @Override
  protected Pca result () {
    return result;
  }

  @Callback
  private void setName () {
    if (result != null)
      result.name (name ());
  }
}
