package edu.dfci.cccb.mev.annotation.elasticsearch.index.csv.test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.elasticsearch.action.get.GetResponse;
import org.junit.Ignore;
import org.junit.Test;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.CsvIndexLoader;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.CsvIndexLoaderConfig;

public class TestCsvIndexLoader extends AbstractTestWithElasticSearch {

//  @Before
//  public void setup () {     
//    client = new TransportClient ().addTransportAddress (
//             new InetSocketTransportAddress ("anton-masha.dfci.harvard.edu", 9300));
//  }
   
  String INDEX_NAME="test_index";
  String INDEX_TYPE="test_type";
  @Test @Ignore
  public void testProcess () throws IndexLoaderException, InterruptedException {
    URL testFile  = TestCsvIndexDocumentParser.class.getResource ("/tcga/tcga_data/clinical/");
    CsvIndexLoaderConfig config = new CsvIndexLoaderConfig (testFile.getPath (), "*.csv", INDEX_NAME, INDEX_TYPE).bulkSize (1);
    
    IndexLoader loader = new CsvIndexLoader (config, client);
    
    loader.process ();
    Thread.sleep (1000);
    // first, try a get request, to make sure there is something in the index
    GetResponse results = client.prepareGet(INDEX_NAME, INDEX_TYPE, "a").execute().actionGet();
    
    // this assertion succeeds, as we expect it to.
    assertThat(results.getId(), is("a"));
    assertTrue(results.isExists ());
  }

  @Test 
  public void testProcessFile () throws IndexLoaderException, URISyntaxException, IOException, InterruptedException {
    URL testFile  = TestCsvIndexDocumentParser.class.getResource ("/tcga/tcga_data/clinical/testStream2.csv");
    CsvIndexLoaderConfig config = new CsvIndexLoaderConfig (testFile.getPath (), "*.csv", INDEX_NAME, INDEX_TYPE).bulkSize (1);
    
    IndexLoader loader = new CsvIndexLoader (config, client);
    Path entry = Paths.get (testFile.toURI ());
    loader.processFile (entry);
    Thread.sleep (1000);
    test();
  }
  
  public void test() throws InterruptedException{
    // first, try a get request, to make sure there is something in the index
    GetResponse results = client.prepareGet(INDEX_NAME, INDEX_TYPE, "e")
            .setOperationThreaded(false).execute().actionGet();
    
    // this assertion succeeds, as we expect it to.
    assertThat(results.getId(), is("e"));
    assertTrue(results.isExists ());
  }
}
