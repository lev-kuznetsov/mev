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

import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import edu.dfci.cccb.mev.heatmap.domain.DataException;
import edu.dfci.cccb.mev.heatmap.domain.DataProvider;

/**
 * @author levk
 * 
 */
public class SuperCsvDataParserTest {

  private SuperCsvDataParser parser = new SuperCsvDataParser ();

  @Test
  public void simple () throws DataException {
    String input = "id\tsa\tsb\tsc\tsd\nga\t0.1\t0.2\t\tNaN\ngb\tNA\tInf\t0.4\t1.5";
    double[] expected = new double[] { 0.1, 0.2, NaN, NaN, NaN, POSITIVE_INFINITY, 1.5, 0.4 };
    int index = 0;
    for (DataProvider<?> provider = parser.parse (new ByteArrayInputStream (input.getBytes ())); provider.next ();)
      assertEquals (expected[index++], provider.value ());
  }

  @Test (expected = DataException.class)
  public void badData () throws DataException {
    String input = "id\tsa\tsb\tsc\tsd\nga\t0.1\t0.2\t\tNaN\ngb\tNA\tInf\thallo\t1.5";
    double[] expected = new double[] { 0.1, 0.2, NaN, NaN, NaN, POSITIVE_INFINITY, 1.5, 0.4 };
    int index = 0;
    for (DataProvider<?> provider = parser.parse (new ByteArrayInputStream (input.getBytes ())); provider.next ();)
      assertEquals (expected[index++], provider.value ());
    fail ();
  }
}
