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
package edu.dfci.cccb.mev.dataset.domain.fs;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.mock.MockUrlInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;

/**
 * @author levk
 * 
 */
public class FlatFileDatasetBuilderTest {

  private SimpleDatasetBuilder builder;

  @Before
  public void initializeBuilder () throws IOException {
    builder = new SimpleDatasetBuilder ();
    builder.setParserFactories (asList (new SuperCsvParserFactory ()));
    builder.setValueStoreBuilder (new  FlatFileValueStoreBuilder());
  }

  @Test 
  public void simpleBuild () throws Exception {
    Dataset set = builder.build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\n" +
                                                           "g1\t.1\t.2\t.3\n" +
                                                           "g2\t.4\t.5\t.6"));
    assertEquals ("mock", set.name ());
    assertEquals (0.1, set.values ().get ("g1", "sa"), 0.0);
  }

  @Test
  public void simpleBuildMissaligned () throws Exception {
    Dataset set = builder.build (new MockTsvInput ("mock", "sa\tsb\tsc\n" +
            "g1\t.1\t.2\t.3\n" +
            "g2\t.4\tx\t.6"));
    assertEquals ("mock", set.name ());
    assertEquals (0.1, set.values ().get ("g1", "sa"), 0.0);
    assertEquals (0.6, set.values ().get ("g2", "sc"), 0.0);
  }

  @Test
  public void simpleBuildWithX () throws Exception {
    Dataset set = builder.build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\n" +
            "g1\t.1\t.2\t.3\n" +
            "g2\t.4\tx\t.6"));
    assertEquals ("mock", set.name ());
    assertEquals (0.1, set.values ().get ("g1", "sa"), 0.0);
  }

  @Test 
  public void buildWithSelection() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException{
    
    List<String> keys = new ArrayList<String> (){
      private static final long serialVersionUID = 1L;
      {
        add("66a354fe-454e-40a6-8464-a7d97236d398_miR_gene_expression");
        add("290f101e-ff47-4aeb-ad71-11cb6e6b9dde_miR_gene_expression");
      }
    };
    
    Selection sourceSelection = new SimpleSelection ("dataset.with.from.selection", new Properties (), keys);    
    URL dataUrl = this.getClass ().getResource ("/data/tcga/tcga_data/ACC/Level_3/ACC.IlluminaHiSeq_miRNASeq.Level_3.Expression-miRNA.readsPerMillionMapped.tsv");        
    assertNotNull (dataUrl);
    
    RawInput rawInput = new MockUrlInput (dataUrl);             
    builder.build (rawInput, sourceSelection);
    
  }
  
  @Test 
  public void buildNoSelectionFilter5cols90K() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException{

    URL dataUrl = this.getClass ().getResource ("/data/tcga/tcga_data/COAD/Level_2/COAD.AgilentG4502A_07_3.Level_2.cols5.tsv");
    assertNotNull (dataUrl);
    RawInput rawInput = new MockUrlInput (dataUrl);             
    builder.build (rawInput);
    
  }
  
  @Test 
  public void buildLimma() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException, InvalidCoordinateException, IOException{

    URL dataUrl = this.getClass ().getResource ("/data/limma/mouse_test_dataset.tsv");
    assertNotNull (dataUrl);
    RawInput rawInput = new MockUrlInput (dataUrl);             
    Dataset dataset = builder.build (rawInput);
    
    assertEquals (16.45, dataset.values ().get ("Actb", "A"), 0.0);
    assertEquals (14.8, dataset.values ().get ("Actg1", "A"), 0.0);
    
    InputStream in = rawInput.input ();
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));    
    String sHeader = reader.readLine();
    String[] header = sHeader.split("\t");    
    String line;    
    while ((line = reader.readLine()) != null) {      
      String[] data = line.split("\t");
      String row = data[0];
      for(int i=1; i<data.length;i++){
        String col = header[i];  
        double val = Double.parseDouble (data[i]);
        assertEquals (val, dataset.values ().get (row, col), 0.0);
      }
    }    
    reader.close();
  }
 
  @Test 
  public void buildWithSelection5colsOutOf180() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException{
    
    List<String> keys = new ArrayList<String> (){
      private static final long serialVersionUID = 1L;
      {
        add("TCGA-A6-3810-01A-01R-1022-07");
        add("TCGA-AA-3845-01A-01R-1022-07");
        add("TCGA-AA-3955-01A-02R-1022-07");
        
      }
    };
    
    Selection sourceSelection = new SimpleSelection ("dataset.with.from.selection", new Properties (), keys);
    
    URL dataUrl = this.getClass ().getResource ("/data/tcga/tcga_data/COAD/Level_2/COAD.AgilentG4502A_07_3.Level_2.cols5.tsv");
    
    assertNotNull (dataUrl);
    
    RawInput rawInput = new MockUrlInput (dataUrl);             
    builder.build (rawInput, sourceSelection);
    
  }

}
