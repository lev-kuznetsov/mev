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
package edu.dfci.cccb.mev.heatmap.domain.concrete.supercsv;

import static edu.dfci.cccb.mev.heatmap.domain.Dimension.COLUMN;
import static edu.dfci.cccb.mev.heatmap.domain.Dimension.ROW;
import static org.supercsv.prefs.CsvPreference.TAB_PREFERENCE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.log4j.Log4j;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvListReader;
import org.supercsv.util.CsvContext;

import edu.dfci.cccb.mev.heatmap.domain.DataException;
import edu.dfci.cccb.mev.heatmap.domain.DataProvider;
import edu.dfci.cccb.mev.heatmap.domain.Projection;
import edu.dfci.cccb.mev.heatmap.domain.concrete.SimpleProjection;
import edu.dfci.cccb.mev.heatmap.domain.prototype.AbstractDataParser;

/**
 * @author levk
 * 
 */
@Log4j
public class SuperCsvDataParser extends AbstractDataParser {

  private static final CellProcessor DOUBLE_PROCESSOR = new DoubleProcessorAggregate ();
  private static final CellProcessor IGNORE_PROCESSOR = new CellProcessorAdaptor () {

    @Override
    public Object execute (Object value, CsvContext context) {
      return null;
    }
  };

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.heatmap.domain.DataParser#parse(java.io.InputStream) */
  @Override
  public DataProvider<?> parse (final InputStream input) throws DataException {
    try {
      return new DataProvider<Double> () {

        private final CsvListReader reader;
        private final String[] header;
        private final CellProcessor[] processors;

        private Iterator<Entry<String, Double>> row;
        private Entry<String, Double> current;
        private String key;

        {
          reader = new CsvListReader (new InputStreamReader (input), TAB_PREFERENCE);
          header = reader.getHeader (true);
          final List<String> first = reader.read ();
          final Map<String, Double> processed = new HashMap<> ();
          processors = new CellProcessor[header.length];
          for (int index = header.length; --index > 0;)
            try {
              double value = (Double) DOUBLE_PROCESSOR.execute (first.get (index), null);
              log.debug ("Processing column " + header[index] + " as a data column");
              processors[index] = DOUBLE_PROCESSOR;
              processed.put (header[index], value);
            } catch (SuperCsvCellProcessorException e) {
              log.debug ("Processing column " + header[index] + " as a non-data column");
              processors[index] = IGNORE_PROCESSOR;
            }
          row = processed.entrySet ().iterator ();

          processors[0] = new CellProcessor () {

            @Override
            public Object execute (Object value, CsvContext context) {
              key = (String) value;
              return null;
            }
          };
          key = first.get (0);
          log.debug ("Processing column " + header[0] + " as an index column");
        }

        @Override
        public boolean next () throws DataException {
          while (!row.hasNext ())
            try {
              Map<String, Double> values = new HashMap<> ();
              List<?> cells = reader.read (processors);
              if (cells == null)
                return false;
              Double value;
              for (int index = header.length; --index >= 0;)
                if ((value = (Double) cells.get (index)) != null)
                  values.put (header[index], value);
              row = values.entrySet ().iterator ();
            } catch (IOException | SuperCsvException e) {
              // TODO Auto-generated catch block
              throw new DataException (e) {
                private static final long serialVersionUID = 1L;
              };
            }
          current = row.next ();
          return true;
        }

        @Override
        public Projection[] projections () {
          return new Projection[] { new SimpleProjection (ROW, key), new SimpleProjection (COLUMN, current.getKey ()) };
        }

        @Override
        public Double value () {
          return current.getValue ();
        }
      };
    } catch (IOException | SuperCsvException e) {
      // TODO Auto-generated catch block
      throw new DataException (e) {
        private static final long serialVersionUID = 1L;
      };
    }
  }
}
