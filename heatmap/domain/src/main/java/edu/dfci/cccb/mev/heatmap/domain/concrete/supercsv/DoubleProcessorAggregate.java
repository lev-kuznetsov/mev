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
import lombok.Delegate;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * @author levk
 * 
 */
public class DoubleProcessorAggregate extends CellProcessorAdaptor {

  private final @Delegate CellProcessor delegate;

  {
    CellProcessor delegate = new ParseDouble ();
    delegate = new NAToNaNProcessor (delegate);
    delegate = new MinusInfToNegativeInfinityProcessor (delegate);
    delegate = new InfToInfinityProcessor (delegate);
    delegate = new ConvertNullTo (NaN, delegate);
    this.delegate = delegate;
  }
}
