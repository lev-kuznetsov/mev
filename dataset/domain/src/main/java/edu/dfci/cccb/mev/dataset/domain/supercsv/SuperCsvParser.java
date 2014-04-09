/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.domain.supercsv;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.io.CsvListReader;
import org.supercsv.util.CsvContext;

import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InputContentStreamException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractParser;

/**
 * @author levk
 * 
 */
public class SuperCsvParser extends AbstractParser implements Closeable {

  private final CellProcessor DOUBLE_CELL_PROCESSOR =
                                                      new CellProcessorAdaptor (new ConvertNullTo (NaN,
                                                                                                   new ParseDouble ())) {

                                                        @Override
                                                        public Object execute (Object value,
                                                                               CsvContext context) {
                                                          if ("Inf".equals (value))
                                                            return POSITIVE_INFINITY;
                                                          else if ("-Inf".equals (value))
                                                            return NEGATIVE_INFINITY;
                                                          else if ("NA".equals (value))
                                                            return NaN;
                                                          else if ("null".equals (value))
                                                            return NaN;
                                                          else
                                                            return next.execute (value,
                                                                                 context);
                                                        }
                                                      };
  private final CellProcessor IGNORE_CELL_PROCESSOR = new CellProcessorAdaptor () {

    @Override
    public Object execute (Object value, CsvContext context) {
      return null;
    }
  };
  private final CellProcessor ROW_ID_PROCESSOR = new CellProcessor () {

    @Override
    public Object execute (Object value, CsvContext context) {
      currentRowName = value.toString ();
      rows.put (currentRowName, currentRowName);
      return null;
    }
  };

  private final CsvListReader reader;
  private final String[] header;
  private CellProcessor[] processors;
  private String currentRowName;
  private String currentColumnName;
  private double currentValue;
  private Iterator<Entry<String, Double>> currentRow;
  private final Map<String, String> rows = new LinkedHashMap<String, String> ();
  private final Map<String, String> columns = new LinkedHashMap<String, String> ();  

  protected SuperCsvParser (CsvListReader reader) throws DatasetBuilderException {
    this.reader = reader;
    try {
      header = reader.getHeader (true);      
      List<String> firstDataLine = reader.read ();
      processors = new CellProcessor[header.length];
      Map<String, Double> firstProcessedLine = new HashMap<> ();      
      for (int index = 1; index < header.length; index++){
        try {
          firstProcessedLine.put (header[index],
                                  (Double) DOUBLE_CELL_PROCESSOR.execute (firstDataLine.get (index), null));
          processors[index] = DOUBLE_CELL_PROCESSOR;
          columns.put (header[index], header[index]);          
        } catch (SuperCsvCellProcessorException e) {
          processors[index] = IGNORE_CELL_PROCESSOR;
        }                
      }         
      processors[0] = ROW_ID_PROCESSOR;
      currentRowName = firstDataLine.get (0);      
      rows.put (currentRowName, currentRowName);
      currentRow = firstProcessedLine.entrySet ().iterator ();
    } catch (IOException e) {
      throw new InputContentStreamException (e);
    }
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.builders.Parser#dimension(edu
   * .dfci.cccb.mev.dataset.domain.contract.Dimension.Type) */
  @Override
  public String projection (Type type) throws InvalidDimensionTypeException {
    switch (type) {
    case ROW:
      return currentRowName;
    case COLUMN:
      return currentColumnName;
    default:
      throw new InvalidDimensionTypeException ().dimension (type);
    }
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.builders.Parser#value() */
  @Override
  public double value () {
    return currentValue;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.builders.Parser#next() */
  @Override
  public boolean next () throws DatasetBuilderException {
    while (!currentRow.hasNext ()){
      try {
        List<?> values = reader.read (processors);
        if (values == null)
          return false;
        Double value;
        Map<String, Double> row = new HashMap<> ();
        for (int index = header.length; --index >= 0;)
          if ((value = (Double) values.get (index)) != null)
            row.put (header[index], value);
        currentRow = row.entrySet ().iterator ();
      } catch (IOException e) {
        throw new InputContentStreamException (e);
      }
    }
    Entry<String, Double> next = currentRow.next ();
    currentColumnName = next.getKey ();    
    currentValue = next.getValue ();
    return true;
  }

  /* (non-Javadoc)
   * @see java.io.Closeable#close() */
  @Override
  public void close () throws IOException {
    reader.close ();
  }

  @Override
  public List<String> columnKeys () {
    return new ArrayList<String>(columns.values());
    
  }

  @Override
  public List<String> rowKeys () { 
    return new ArrayList<String>(rows.values());
  }
}