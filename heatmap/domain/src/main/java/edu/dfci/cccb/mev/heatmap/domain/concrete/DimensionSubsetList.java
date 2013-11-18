package edu.dfci.cccb.mev.heatmap.domain.concrete;

import java.util.ArrayList;

import lombok.Getter;
import edu.dfci.cccb.mev.heatmap.domain.DimensionSubset;

public class DimensionSubsetList<T> extends ArrayList<T> implements DimensionSubset<T> {

  @Getter private String name;
  @Getter private String description;
  @Getter private String color;
  
  public DimensionSubsetList(String name, String description, String color){
    this.name=name;
    this.description=description;
    this.color=color;
  }
  
}
