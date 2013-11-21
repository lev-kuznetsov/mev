package edu.dfci.cccb.mev.dataset.domain.mock;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractSelections;


public class SelectionsMock extends AbstractSelections {

  protected final Map<String, Selection> selectionsMap;
  
  
  protected SelectionsMock(){
    selectionsMap=new LinkedHashMap<String, Selection> ();
  }
  
  @Override
  public synchronized void put (Selection selection) {
    selectionsMap.put (selection.name (), selection);
  }

  @Override
  public synchronized void put (String name, Properties properties, List<String> keys) {
     this.put(new SelectionMock (name, properties, keys));
  }
  
  @Override
  public Selection get (String name) throws SelectionNotFoundException {

    if (selectionsMap.containsKey (name))
      return selectionsMap.get (name);
    else
      throw new SelectionNotFoundException ();
  }

  @Override
  public synchronized void remove (String name) throws SelectionNotFoundException {
    if (selectionsMap.containsKey (name))
      selectionsMap.remove (name);
    else
      throw new SelectionNotFoundException ();
  }

  @Override
  public Collection<String> list () {
    return selectionsMap.keySet ();
  }

}
