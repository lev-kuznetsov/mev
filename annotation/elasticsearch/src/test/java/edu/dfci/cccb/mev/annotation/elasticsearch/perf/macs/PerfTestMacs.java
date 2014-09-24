package edu.dfci.cccb.mev.annotation.elasticsearch.perf.macs;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lombok.extern.log4j.Log4j;

import org.databene.contiperf.PerfTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.index.eav.test.EavTestBag;
import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractElasticsearchPerfTest;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.IndexAdminHelperImpl;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexAdminHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.CsvIndexLoader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavIndexDocumentParserFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavLoaderConfig;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavParser;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.mock.MockDataGenerator;
//field count: 1544

//run 1 - 630K records (too many)
//samples: 10
//max:     30194
//average: 26764.9
//median:  26643

//run 2 630K records (too many)
//samples: 10
//max:     29670
//average: 25866.0
//median:  25853

//run 1 - 126678 unique patient+visits;
//samples: 10
//max:     3117
//average: 2702.2
//median:  2651

@Log4j
public class PerfTestMacs extends AbstractElasticsearchPerfTest {
    
  //total lines vefore fix: 17,512,894
  //lines with bad CR: 4478
  /************
1376696:1420927  2208  0 OTHOUT1 First other Non-AIDS dx 730.90  730|Osteomyelitis, periostitis, and other infections involving bone Excludes: jaw (526.4-526.5) petrous bone (383.2) Use additional code to identify organism, such as Staphylococcus (041.1) The following fifth-digit subclassification is for use with category 730; valid digits are in [brackets] under each code.  See list at beginning of chapter for de|730.9|Unspecified infection of bone [0-9]
1376699:1420930 2208  0 OTHOUT2 Second other Non-AIDS dx  715.80  715|Osteoarthrosis and allied disorders Note: Localized, in the subcategories below, includes bilateral involvement of the same site. Includes: arthritis or polyarthritis: degenerative hypertrophic degenerative joint disease osteoarthritis Excludes: Marie-Strümpell spondylitis (720.0) osteoarthrosis [osteoarthritis] of spine (721.0-721.9) The following fifth-digit subclassification is for use with category 715; valid digits are in [brackets] under each code.  See list at beginning of chapter for definitions: 0 site unspecified 1 shoulder region 2 upp|715.8|Osteoarthrosis involving, or with mention of more than one site, but not specified as generalized [0,9]
|136.3|Pneumocystosis Pneumonia due to Pneumocystis carinii Pneumonia due to Pneumocystis jiroveci
1376810:1421057 2210  0 OTHOUT1 First other Non-AIDS dx 715.90  715|Osteoarthrosis and allied disorders Note: Localized, in the subcategories below, includes bilateral involvement of the same site. Includes: arthritis or polyarthritis: degenerative hypertrophic degenerative joint disease osteoarthritis Excludes: Marie-Strümpell spondylitis (720.0) osteoarthrosis [osteoarthritis] of spine (721.0-721.9) The following fifth-digit subclassification is for use with category 715; valid digits are in [brackets] under each code.  See list at beginning of chapter for definitions: 0 site unspecified 1 shoulder region 2 upper arm 3|715.9|Osteoarthrosis, unspecified whether generalized or localized [0-8]
|798.9|Unattended death Death in circumstances where the body of the deceased was found and no cause could be discovered Found dead
|518.8|Other diseases of lung
|799.4|Cachexia Wasting disease Code first underlying condition, if known
|799.9|Other unknown and unspecified cause Undiagnosed disease, not specified as to site or system involved Unknown cause of morbidity or mortality
|031.8|Other specified mycobacterial diseases
|279.3|Unspecified immunity deficiency

  ****/
  private final EavLoaderConfig config;
  private final Random random; 
  private List<String> fields;
  public PerfTestMacs(){
    String[] entityIdField = {"case_id", "visit_id"};
    config = new EavLoaderConfig (PerfTestMacs.class.getResource ("/macs/data/").getPath (), 
                                "macs_data_dumpTop10K.tsv", 
                                "macs_index", 
                                "macs_type", 
                                entityIdField, 
                                "variable", 
                                "num_value");
    random=new Random ();
  }
  
  @Before
  public void setUp () throws Exception {
    
    if(!adminHelper.exists (config.indexName ())){
      log.debug("*** Deleting " + config.indexName ());
      throw new Exception(String.format("Index %s does not exist", config.indexName ()));
//      adminHelper.deleteIndex (config.indexName());      
//      log.debug(String.format("*** Loading %s from folder %s file %s", config.indexName()
//                              , config.folderPath (), config.fileFilter ()));
//      CsvIndexLoader loader = new CsvIndexLoader (config, client, new EavIndexDocumentParserFactory ());
//      loader.process ();
    }
    
    fields = getFieldNames();
  }

  private List<String> getFieldNames() throws IOException, IndexAdminException{
    IndexAdminHelper adminHelper = new IndexAdminHelperImpl (client);
    Map<String, Object> mapping = adminHelper.getMapping (config.indexName (), config.typeName ());
    
    Map<String, Object> properties = (Map<String, Object>) mapping.get ("properties");
    List<String> fields = new ArrayList<String> (properties.keySet ().size ());
    for(String field : properties.keySet ()){
      fields.add (field);
    }
    log.debug (String.format("field count: %s ", fields.size ()));
    return fields;
  }

  @Test @PerfTest(invocations=10, threads=1)
  public void test () throws IOException, IndexAdminException, IndexLoaderException {
    String fieldName = fields.get (random.nextInt (fields.size ()));
    log.debug(String.format("Field: %s", fieldName));
    adminHelper.numerifyField (config.indexName (), config.typeName (), fieldName
                               , bulkProcessorFactory.create (
                                bulkProcessorFactory.calculateBulkRows (fields.size ()), 8));
  }
  
  @After
  public void tearDown () throws Exception {
    client.close ();
  }

}
