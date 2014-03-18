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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Parser;

/**
 * @author levk
 * 
 */
public class SuperCsvParserFactoryTest {

  @Test
  public void testParser () throws Exception {
    Parser p = new SuperCsvParserFactory ().parse (new ByteArrayInputStream (("id\tsa\tsb\tsc\n" +
                                                                              "g1\t.1\t.2\t.3\n" +
                                                                              "g2\t.4\t.5\t.6").getBytes ()));
    for (int index = 1; index < 7; index++) {
      assertTrue (p.next ());
      assertEquals (index / 10.0, p.value (), 0.0);
    }
    assertFalse (p.next ());
    
    List<String> expectedColumns = new ArrayList<String>(){
      private static final long serialVersionUID = 1L;

    {      
      add("sa");
      add("sb");
      add("sc");
    }};    
    assertThat(p.columnKeys (), is(expectedColumns));
    
    List<String> expectedRows= new ArrayList<String>(){
      private static final long serialVersionUID = 1L;

    {
      add("g1");
      add("g2");
    }};    
    assertThat(p.rowKeys (), is(expectedRows));
    
  }
}
