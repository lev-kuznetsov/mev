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

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.ROW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Parser;
import edu.dfci.cccb.mev.dataset.domain.gzip.GzipTsvInput;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;

/**
 * @author levk
 * 
 */
@Log4j
public class SuperCsvParserFactoryTest {

  @Test
  public void testParser () throws Exception {
    Parser p = new SuperCsvParserFactory ().parse (new ByteArrayInputStream (("\t\tsa\tsb\tsc\n" +
                                                                              "g1\tp1\t.1\t.2\t.3\n" +
                                                                              "g2\tp2\t.4\t.5\t.6").getBytes ()));
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
  
  @Test
  public void testGEOParser () throws Exception {
//    String url ="ftp://ftp.ncbi.nlm.nih.gov/geo/datasets/GDS4nnn/GDS4092/soft/GDS4092.soft.gz";
    URL url = this.getClass ().getResource ("/geods/geo.gds.soft.txt");
    Parser p = new SuperCsvParserFactory ().parse (new UrlTsvInput (url).input ());
    while(p.next ()) {
//      log.debug (p.value () + ":" + p.projection (ROW)+ ":" + p.projection (COLUMN));
    }
    List<String> expectedRows = new ArrayList<String>(){
      private static final long serialVersionUID = 1L;
      {      
        add("1007_s_at");
        add("1053_at");
        add("117_at");
      }
    };
    assertFalse(p.next ());
    assertThat(p.rowKeys (), is(expectedRows));
    
    List<String> expectedColumns = Arrays.asList ("GSM564960\tGSM564961\tGSM564952\tGSM564958\tGSM564953\tGSM564959\tGSM564956\tGSM564957\tGSM564950\tGSM564954\tGSM564951\tGSM564955".split ("\t"));
    assertThat(p.columnKeys (), is(expectedColumns));
    
  }
}
