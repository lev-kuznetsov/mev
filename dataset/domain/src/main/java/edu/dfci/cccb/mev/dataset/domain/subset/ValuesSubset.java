package edu.dfci.cccb.mev.dataset.domain.subset;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

import lombok.SneakyThrows;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.fs.IFlatFileValues;

public class ValuesSubset implements Values, IFlatFileValues {

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

	@Override
	@SneakyThrows({IOException.class})
	public InputStream asInputStream() throws FileNotFoundException {		
		final PipedOutputStream out = new PipedOutputStream();
		new Thread(
				new Runnable() {
					
					@Override
					@SneakyThrows({IOException.class, InvalidCoordinateException.class})
					public void run() {
						try{
							for(String row : rows){
								for(String column : columns){
									byte[] bytes = new byte[8];								
									ByteBuffer.wrap(bytes).putDouble(values.get(row, column));
									out.write(bytes);								
								}
							}
						}finally{
							out.close();
						}
					}
				}
		).start();		
		return new PipedInputStream(out);
		
	}

}
