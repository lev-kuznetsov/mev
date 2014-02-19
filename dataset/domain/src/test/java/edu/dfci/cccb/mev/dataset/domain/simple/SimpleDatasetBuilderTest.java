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
package edu.dfci.cccb.mev.dataset.domain.simple;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.mock.MockUrlInput;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;

/**
 * @author levk
 * 
 */
public class SimpleDatasetBuilderTest {

  private SimpleDatasetBuilder builder;

  @Before
  public void initializeBuilder () {
    builder = new SimpleDatasetBuilder ();
    builder.setParserFactories (asList (new SuperCsvParserFactory ()));
    builder.setValueStoreBuilder (new MapBackedValueStoreBuilder ());
  }

  @Test @Ignore
  public void simpleBuild () throws Exception {
    Dataset set = builder.build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\n" +
                                                           "g1\t.1\t.2\t.3\n" +
                                                           "g2\t.4\t.5\t.6"));
    assertEquals ("mock", set.name ());
    assertEquals (0.1, set.values ().get ("g1", "sa"), 0.0);
  }
  
  @Test 
  public void buildWithSelection() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException{
    
    List<String> keys = new ArrayList<String> (){
      private static final long serialVersionUID = 1L;
      {
        add("66a354fe-454e-40a6-8464-a7d97236d398_miR_gene_expression");
        add("9635a2c4-fd8b-4b4c-9338-def32b6dd8d3_miR_gene_expression");
        add("1e1ae4dd-1f89-42bd-8154-165f603962b9_miR_gene_expression");
        add("b3794e62-c4bc-43b1-97ca-b6f6e01aa026_miR_gene_expression");
        add("c986ab6f-912a-424f-9957-ff34452339c5_miR_gene_expression");
        add("25a9fd1a-0ff1-434b-bc4b-b78834750994_miR_gene_expression");
        add("b5a63033-f14b-4040-b2b2-8acf9c9ba6d4_miR_gene_expression");
        add("6a8f9385-2064-4d8c-9af1-d2bed53afebb_miR_gene_expression");
        add("e4fe4b0b-198a-46b2-adb3-2f0b140502f8_miR_gene_expression");
      }
    };
    
    Selection sourceSelection = new SimpleSelection ("dataset.with.from.selection", new Properties (), keys);    
    URL dataUrl = this.getClass ().getResource ("/tcga/tcga_data/ACC/Level_3/ACC.IlluminaHiSeq_miRNASeq.Level_3.Expression-miRNA.readsPerMillionMapped.tsv");        
    assertNotNull (dataUrl);
    
    RawInput rawInput = new MockUrlInput (dataUrl);             
    builder.build (rawInput, sourceSelection);
    
  }
  
  @Test @Ignore
  public void buildNoSelectionFilter5cols90K() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException{

    URL dataUrl = this.getClass ().getResource ("/tcga/tcga_data/COAD/Level_2/COAD.AgilentG4502A_07_3.Level_2.cols5.tsv");
    assertNotNull (dataUrl);
    RawInput rawInput = new MockUrlInput (dataUrl);             
    builder.build (rawInput);
    
  }
 
  @Test @Ignore
  public void buildWithSelection5colsOutOf180() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException{
    
    List<String> keys = new ArrayList<String> (){
      private static final long serialVersionUID = 1L;
      {
        add("TCGA-A6-2677-01A-01R-0821-07");
        add("TCGA-AA-3560-01A-01R-0821-07");
        add("TCGA-AA-3529-01A-02R-0821-07");
        add("TCGA-A6-2678-01A-01R-0821-07");
        add("TCGA-AA-3534-01A-01R-0821-07");
       
      }
    };
    
    Selection sourceSelection = new SimpleSelection ("dataset.with.from.selection", new Properties (), keys);
    
    URL dataUrl = this.getClass ().getResource ("/tcga/tcga_data/COAD/Level_2/COAD.AgilentG4502A_07_3.Level_2.tsv");
    
    assertNotNull (dataUrl);
    
    RawInput rawInput = new MockUrlInput (dataUrl);             
    builder.build (rawInput, sourceSelection);
    
  }

}
