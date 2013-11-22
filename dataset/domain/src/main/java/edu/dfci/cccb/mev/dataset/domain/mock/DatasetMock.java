package edu.dfci.cccb.mev.dataset.domain.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Analyses;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDataset;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValues.Coordinate;

@Accessors (fluent = true)
public class DatasetMock extends AbstractDataset {

  private @Getter Values values;
  Dimension columns;
  Dimension rows;  
  
  Random random = new Random();
  
  public DatasetMock(String name, String csvRowNames, String csvColumnNames) throws InvalidDatasetNameException{
    super(name);
    HashMap<MapBackedValues.Coordinate, Double> values = new HashMap<MapBackedValues.Coordinate, Double>();
    
    List<String> rowNames = new ArrayList<String> ();
    for(String letter : csvRowNames.split (",")){
      rowNames.add ("gene-"+letter);
    }    
    List<String> columnNames = new ArrayList<String> ();
    for(String letter : csvColumnNames.split (",")){
      columnNames.add ("sample-"+letter);
    }
    for(String rowName : rowNames){      
      for(String columnName : columnNames){
        values.put (new Coordinate(rowName, columnName), random.nextDouble ());
      }
    }
    this.values = new MapBackedValues (values);
    this.columns = new DimensionMock (Dimension.Type.COLUMN, columnNames);
    this.rows = new DimensionMock (Dimension.Type.ROW, rowNames);
  }

  @Override
  public Dimension dimension (Type type) {
    // TODO Auto-generated method stub
    return type ==Type.COLUMN ? columns : rows;
  }

  @Override
  public Analyses analyses () {
    // TODO Auto-generated method stub
    return null;
  }

  public Dimension columns(){
    return columns;
  }
}
