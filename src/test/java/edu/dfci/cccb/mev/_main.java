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
package edu.dfci.cccb.mev;

import java.io.FileInputStream;
import java.text.NumberFormat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import us.levk.math.linear.HugeRealMatrix;
import us.levk.math.linear.util.RealMatrixJsonSerializer;

/**
 * @author levk
 * 
 */
public class _main {

  public static void main (String[] args) throws Exception {
    try (HugeRealMatrix m = new HugeRealMatrix (new FileInputStream ("/Users/levk/Documents/affy.noannot.txt"),
                                                "\t,".toCharArray (),
                                                "\n".toCharArray (),
                                                NumberFormat.getNumberInstance ());
         JsonGenerator g = new JsonFactory ().createGenerator (System.out)) {
      new RealMatrixJsonSerializer ().serialize (m, g, null);
    }
  }
}
