package edu.dfci.cccb.mev.dataset.domain.mock;

import java.util.List;

import edu.dfci.cccb.mev.dataset.domain.contract.Annotation;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Selections;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDimension;

public class DimensionMock extends AbstractDimension {

  protected Selections selections;
  protected List<String> keys;
  
  public DimensionMock(Dimension.Type type, List<String> keys){
    super(type);
    this.selections = new SelectionsMock ();
    this.keys = keys;
  }
  
  
  @Override
  public Selections selections () {
    return selections;
  }

  @Override
  public Annotation annotation () {
    return null;
  }


  @Override
  public List<String> keys () {
    return keys;
  }
}
