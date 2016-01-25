package edu.dfci.cccb.mev.dataset.domain.subset;

import java.util.List;

import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;

public class ValuesSubset implements Values {

  private final Values values;
  private final List<String> columns;
  private final List<String> rows;
  private Boolean skipJson;
  public ValuesSubset (Values values, List<String> columns, List<String> rows) {
    this(values, columns, rows, values.skipJson ());
  }

  public ValuesSubset (Values values, List<String> columns, List<String> rows, boolean skipJson) {
    this.values = values;
    this.columns = columns;
    this.rows = rows;
    this.skipJson = skipJson;
  }
  
  @Override
  public double get (String row, String column) throws InvalidCoordinateException {
    if(!columns.contains (column) || !rows.contains (row))
      throw new InvalidCoordinateException(String.format("Subset does not contain coordinate (%s, %s)", row, column));
    return values.get(row, column);
  }

  @Override
  public boolean skipJson () {
    return this.skipJson;
  }

}
