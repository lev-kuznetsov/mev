package edu.dfci.cccb.mev.heatmap.domain.concrete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import edu.dfci.cccb.mev.heatmap.domain.Annotation;
import edu.dfci.cccb.mev.heatmap.domain.Dimension;
import edu.dfci.cccb.mev.heatmap.domain.DimensionHeader;
import edu.dfci.cccb.mev.heatmap.domain.DimensionSubset;

public class DimensionHeaderSimple<T> implements DimensionHeader<T> {

  @Getter private final Dimension dimension;
  @Getter private final Map <String, DimensionSubset<T>> keysets;
  @Getter private final Annotation annotation;
  
  public DimensionHeaderSimple(Dimension dimension, Annotation annotation){
    this.dimension = dimension;
    this.keysets = new HashMap<String, DimensionSubset<T>> ();
    this.annotation=annotation;
  }
  
  @Override
  public Annotation getAnnotation () {
    // TODO Auto-generated method stub
    return annotation;
  }

  
  @Override
  public synchronized void addKeyset (DimensionSubset<T> keyset) {
    keysets.put (keyset.getName (), keyset);
  }
  
  @Override
  public Set<String>getKeysetNames(){
    return keysets.keySet ();
  }
  
  @Override
  public List<DimensionSubset<T>>getKeysetList(){
    return  new ArrayList<DimensionSubset<T>>(keysets.values ());
  }
  
}
