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

import java.io.ByteArrayInputStream;

import org.supercsv.cellprocessor.ParseDouble;

import edu.dfci.cccb.mev.domain.Heatmap;
import edu.dfci.cccb.mev.domain.Heatmap.Builder;

/**
 * @author levk
 * 
 */
public class _main {

  public static void main (String[] args) throws Exception {
    try (Heatmap m = new Builder ().allowComments (false)
                                   .allowEmptyLines (false)
                                   .annotationProcessor (null)
                                   .valueProcessor (new ParseDouble ())
                                   .build (new ByteArrayInputStream (("\t\tone\ttwo\tthree\n" +
                                                                      "uno1\tuno2\t1\t2\t3\n" +
                                                                      "duo1\tduo2\t4\t5\t6").getBytes ()))) {
      System.out.println (m.getData (0, 50, 0, 50).values ());
    }
  }
}
