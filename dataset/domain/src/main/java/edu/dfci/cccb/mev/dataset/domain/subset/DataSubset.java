package edu.dfci.cccb.mev.dataset.domain.subset;

import java.io.IOException;
import java.util.List;

import lombok.SneakyThrows;
import edu.dfci.cccb.mev.dataset.domain.contract.Analyses;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDataset;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListAnalyses;

public class DataSubset extends AbstractDataset implements Dataset {

  private final Dataset parent;
  private final Dimension columns;
  private final Dimension rows;
  private final Analyses analyses;
  
  @SneakyThrows(value = {InvalidDimensionTypeException.class})
  public DataSubset(String name, Dataset parent, List<String> columns, List<String> rows) throws InvalidDatasetNameException {	
    super(name);
    this.parent = parent;
    this.columns = parent.dimension (Type.COLUMN).subset (columns);
    this.rows = parent.dimension (Type.ROW).subset (rows);
	//if name is provided, assume we are creating a new dataset with its own set of analyses
    this.analyses = new ArrayListAnalyses ();
  }
  

  @SneakyThrows({InvalidDimensionTypeException.class}) 
  public DataSubset(Dataset parent, List<String> columns, List<String> rows) throws InvalidDatasetNameException {
    super(parent.name());    
    this.parent = parent;
    this.columns = parent.dimension (Type.COLUMN).subset (columns);
    this.rows = parent.dimension (Type.ROW).subset (rows);    
	//if name is NOT provided, assume we are just asking for a subset of data from an existing dataset
	//so keep its analysis intact
    this.analyses = parent.analyses();
  }
    
  @Override
  public Values values () {
    return new ValuesSubset (parent.values (), columns.keys (), rows.keys (), false);    
  }

  @Override
  public Dimension dimension (Type type) throws InvalidDimensionTypeException {
    if(type == Type.COLUMN)
      return columns;
    else if(type == Type.ROW)
      return rows;
    else
      throw new InvalidDimensionTypeException ();     
  }

  @Override
  public void set (Dimension dimension) throws InvalidDimensionTypeException {
    throw new UnsupportedOperationException ("Cannot change dimension of a subset");
  }

  @Override
  public Analyses analyses () {
    return this.analyses;
  }

  @Override
  public void exportSelection (String name,
                               Type dimension,
                               String selection,
                               Workspace workspace,
                               DatasetBuilder datasetBuilder) throws InvalidDimensionTypeException,
                                                             SelectionNotFoundException,
                                                             InvalidCoordinateException,
                                                             IOException,
                                                             DatasetBuilderException,
                                                             InvalidDatasetNameException {
    exportSelection (name, dimension, selection, workspace, datasetBuilder);
  }
  

  @Override
  public void close () throws Exception {
    // Closing of subset should not close the parent
    // Nothing to do here.
  }

  

}
