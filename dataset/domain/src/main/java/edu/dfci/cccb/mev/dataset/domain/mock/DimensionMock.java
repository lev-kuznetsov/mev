package edu.dfci.cccb.mev.dataset.domain.mock;

import java.util.List;

import edu.dfci.cccb.mev.dataset.domain.contract.Annotation;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Selections;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDimension;

public class DimensionMock extends AbstractDimension {

  protected Selections selections;
  
  public DimensionMock(Dimension.Type type, List<String> keys){
    super(type, keys);
    this.selections = new SelectionsMock ();
  }
  
  
  @Override
  public Selections selections () {
    return this.selections;
  }

  @Override
  public Annotation annotation () {
    return null;
  }
}
