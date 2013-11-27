package edu.dfci.cccb.mev.dataset.domain.simple;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractWorkspace;

public class WorkspaceBackedByMap extends AbstractWorkspace {

  private final Map<String, Dataset> datasetsMap;
  
  public WorkspaceBackedByMap(){
    datasetsMap = new LinkedHashMap<String, Dataset> ();
  }
  
  @Override
  public synchronized void put (Dataset dataset) {
    datasetsMap.put (dataset.name (), dataset);
  }

  @Override
  public Dataset get (String name) throws DatasetNotFoundException {
    if(datasetsMap.containsKey (name))
      return datasetsMap.get (name);
    else
      throw new DatasetNotFoundException ();
  }

  @Override
  public synchronized void remove (String name) throws DatasetNotFoundException {
    if(datasetsMap.containsKey (name))
      datasetsMap.remove (name);
    else
      throw new DatasetNotFoundException ();
  }

  @Override
  public List<String> list () {
    return new ArrayList<String> (datasetsMap.keySet ());
  }

}
