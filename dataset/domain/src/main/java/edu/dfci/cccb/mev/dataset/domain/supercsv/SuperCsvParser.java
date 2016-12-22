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
import java.sql.RowId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.NoArgsConstructor;
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
                                                          else if ("x".equals (value))
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
  public interface RowIdParser{
      String parse(String value);
  }
  private RowIdParser rowIdParser = null;
  private String parseRowId(Object value){
      return rowIdParser==null
              ? value.toString()
              : rowIdParser.parse(value.toString ());
  }
  private final CellProcessor ROW_ID_PROCESSOR = new CellProcessor () {

    @Override
    public Object execute (Object value, CsvContext context) {
      currentRowName = parseRowId(value);
      rows.put (currentRowName, rows.size ());
      return null;
    }
  };

  private CsvListReader reader;
  private String[] header;
  private CellProcessor[] processors;
  private String currentRowName;
  private String currentColumnName;
  private double currentValue;
  private Iterator<Entry<String, Double>> currentRow;
  private final Map<String, Integer> rows = new LinkedHashMap<String, Integer> ();
  private final Map<String, Integer> columns = new LinkedHashMap<String, Integer> ();
  
  protected SuperCsvParser (CsvListReader reader) throws DatasetBuilderException {
      this(reader, null);
  }
  protected SuperCsvParser (CsvListReader reader, RowIdParser rowIdParser) throws DatasetBuilderException {
      this.rowIdParser = rowIdParser;
      this.reader = reader;
      try {
          String tmpHeader[] = reader.getHeader (true);
          List<String> firstDataLine = reader.read ();
          if(tmpHeader.length == firstDataLine.size()-1){
              header = new String[firstDataLine.size()];
              header[0] = "id";
              for(int i=0; i<tmpHeader.length; i++)
                  header[i+1]=tmpHeader[i];
          }else{
              header = tmpHeader;
          }

          processors = new CellProcessor[header.length];
          //use LinkedHashMap to preserve the order of keys
          Map<String, Double> firstProcessedLine = new LinkedHashMap<> ();
          for (int index = 1; index < header.length; index++){
              try {
                  firstProcessedLine.put (header[index],
                          (Double) DOUBLE_CELL_PROCESSOR.execute (firstDataLine.get (index), null));
                  processors[index] = DOUBLE_CELL_PROCESSOR;
                  columns.put (header[index], columns.size ());
              } catch (SuperCsvCellProcessorException e) {
                  processors[index] = IGNORE_CELL_PROCESSOR;
              }
          }
          processors[0] = ROW_ID_PROCESSOR;
          currentRowName = parseRowId(firstDataLine.get (0));
          rows.put (currentRowName, rows.size ());
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
        //use LinkedHashMap to preserve the order of keys
        Map<String, Double> row = new LinkedHashMap<String, Double> ();
        for (int index = 1; index < header.length; index++)
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
    List<String> keys = new ArrayList<String>(columns.size ());
    for(String key : columns.keySet ()){
        keys.add (key);
    }
    return keys;
  }

  @Override
  public List<String> rowKeys () { 
    List<String> keys = new ArrayList<String>(columns.size ());
    for(String key : rows.keySet ()){
        keys.add (key);
    }
    return keys;
  }
  
  @Override
  public Map<String, Integer> rowMap () { 
    return rows;
  }
  
  @Override
  public Map<String, Integer> columnMap () { 
    return columns;
  }
}
