package edu.dfci.cccb.mev.dataset.domain.mock;

import java.util.List;
import java.util.Properties;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractSelection;

public class SelectionMock extends AbstractSelection {

  private String name;
  private Properties properties;
  private List<String> keys;

  public SelectionMock(String name, Properties properties, List<String> keys){
    this.name=name;
    this.properties=properties;
    this.keys=keys;
  }
  
  @Override
  public String name () {
    // TODO Auto-generated method stub
    return name;
  }

  @Override
  public Properties properties () {
    // TODO Auto-generated method stub
    return properties;
  }

  @Override
  public List<String> keys () {
    // TODO Auto-generated method stub
    return keys;
  }

  @Override
  public Dataset export (String name) {
    // TODO Auto-generated method stub
    return null;
  }

}
