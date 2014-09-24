package edu.dfci.cccb.mev.annotation.elasticsearch.index.csv.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.eclipse.jetty.util.log.Log;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParser;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.CsvIndexDocumentParser;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.CsvIndexLoaderConfig;

@Log4j
public class TestCsvIndexDocumentParser extends AbstractTestWithElasticSearch {

 
  @ToString
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor  
  private static class DummyJson{    
    @JsonProperty String f1;
    @JsonProperty String f2;
    @JsonProperty String id;
  }  
  
  @Test
  public void test () throws IOException, URISyntaxException {
    
    URL testFile  = TestCsvIndexDocumentParser.class.getResource ("/tcga/tcga_data/clinical/testStream.csv");
    CsvIndexLoaderConfig config = new CsvIndexLoaderConfig (testFile.getPath (), "*.csv", "test_index", "test_type", "id".split (",")); 
    IndexDocumentParser stream = new CsvIndexDocumentParser (Paths.get (testFile.toURI ()), config);
    
    Iterator<XContentBuilder> it=stream.iterator ();
    
    assertTrue (it.hasNext ());
    XContentBuilder jsonBuilder = it.next ();    
    ObjectMapper mapper = new ObjectMapper();
    DummyJson json = mapper.readValue(jsonBuilder.string (), DummyJson.class);
    DummyJson expected = new DummyJson ("y", "z", "x");
    
    log.debug("jsonBuilder.string() "+jsonBuilder.string ());
    log.debug ("expected:"+expected);
    log.debug ("actual:"+json);
    assertThat(expected, is(json));
    
    assertTrue (it.hasNext ());
    jsonBuilder = it.next ();        
    json = mapper.readValue(jsonBuilder.string (), DummyJson.class);
    expected = new DummyJson ("b", "c", "a");
    assertThat(expected, is(json));
    
    assertFalse (it.hasNext ());
  }

}
