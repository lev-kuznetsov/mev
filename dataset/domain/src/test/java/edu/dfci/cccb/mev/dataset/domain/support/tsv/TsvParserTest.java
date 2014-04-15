/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.dataset.domain.support.tsv;

import static edu.dfci.cccb.mev.dataset.domain.support.tsv.TsvParser.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.support.tsv.TsvParser.ROW;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Values.Value;
import edu.dfci.cccb.mev.dataset.domain.support.Consumer;
import edu.dfci.cccb.mev.dataset.domain.support.Parser;

/**
 * @author levk
 */
public class TsvParserTest {

  private final TsvParser parser = new TsvParser ();

  @SuppressWarnings ("unchecked")
  @Test
  public void simpleTsv () throws Exception {
    final int[] count = new int[] { 0 };
    parser.parse (new ByteArrayInputStream (("id\tc1\tc2\n" +
                                             "r1\t.1\t.2\n" +
                                             "r2\t.2\t.3\n" +
                                             "r3\t.3\t.4").getBytes ()),
                  new Consumer<Value<String, Double>> () {
                    private Iterator<String> columns = asList ("c1", "c2", "c1", "c2", "c1", "c2").iterator ();
                    private Iterator<String> rows = asList ("r1", "r1", "r2", "r2", "r3", "r3").iterator ();
                    private Iterator<Double> values = asList (.1, .2, .2, .3, .3, .4).iterator ();

                    public void consume (Value<String, Double> entity) throws IOException {
                      assertThat (entity.coordinates ().get (ROW), is (rows.next ()));
                      assertThat (entity.coordinates ().get (COLUMN), is (columns.next ()));
                      assertThat (entity.value (), is (values.next ()));
                      count[0]++;
                    }
                  }, asList (new Consumer<String> () {
                    private Iterator<String> rows = asList ("r1", "r2", "r3").iterator ();

                    public void consume (String entity) throws IOException {
                      assertThat (entity, is (rows.next ()));
                    }
                  }, new Consumer<String> () {
                    private Iterator<String> columns = asList ("c1", "c2").iterator ();

                    public void consume (String entity) throws IOException {
                      assertThat (entity, is (columns.next ()));
                    }
                  }).toArray (new Consumer[0]),
                  Parser.STRING, Parser.DOUBLE);
    assertThat (count[0], is (6));
  }

  @SuppressWarnings ("unchecked")
  @Test (expected = NumberFormatException.class)
  public void badValue () throws Exception {
    final int[] count = new int[] { 0 };
    try {
      parser.parse (new ByteArrayInputStream (("id\tc1\tc2\n" +
                                               "r1\t.h\t.2\n" +
                                               "r2\t.2\t.3\n" +
                                               "r3\t.3\t.4").getBytes ()),
                    new Consumer<Value<String, Double>> () {
                      private Iterator<String> columns = asList ("c1", "c2", "c1", "c2", "c1", "c2").iterator ();
                      private Iterator<String> rows = asList ("r1", "r1", "r2", "r2", "r3", "r3").iterator ();
                      private Iterator<Double> values = asList (.1, .2, .2, .3, .3, .4).iterator ();

                      public void consume (Value<String, Double> entity) throws IOException {
                        assertThat (entity.coordinates ().get (ROW), is (rows.next ()));
                        assertThat (entity.coordinates ().get (COLUMN), is (columns.next ()));
                        assertThat (entity.value (), is (values.next ()));
                        count[0]++;
                      }
                    }, asList (new Consumer<String> () {
                      private Iterator<String> rows = asList ("r1", "r2", "r3").iterator ();

                      public void consume (String entity) throws IOException {
                        assertThat (entity, is (rows.next ()));
                      }
                    }, new Consumer<String> () {
                      private Iterator<String> columns = asList ("c1", "c2").iterator ();

                      public void consume (String entity) throws IOException {
                        assertThat (entity, is (columns.next ()));
                      }
                    }).toArray (new Consumer[0]),
                    Parser.STRING, Parser.DOUBLE);
      fail ();
    } catch (IOException e) {
      throw ((Exception) e.getCause ());
    }
  }
}
