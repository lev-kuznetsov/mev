package edu.dfci.cccb.mev.annotation.elasticsearch.index.csv.test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentHelper;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexLoaderException;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.CsvIndexDocumentHelper;

@Log4j
public class TestCsvIndexDocumentHelper {

  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor  
  private static class ExpectedJsonDoc{
    @JsonProperty String f1;
    @JsonProperty String f2;
    @JsonProperty String id;
  }
  
  @Test
  public void testProcess () throws IndexLoaderException, JsonParseException, JsonMappingException, IOException {
    IndexDocumentHelper csvHelper = new CsvIndexDocumentHelper (new ArrayList<String> (){
      {add ("f1");add("f2");add("id");}      
    }, "id".split (","));
    
    XContentBuilder jsonBuilder = csvHelper.process (new String[] {"x", "y", "z"});      
    log.debug ("jsonBuilder.string(): "+jsonBuilder.string ());
    
    ObjectMapper mapper = new ObjectMapper();
    ExpectedJsonDoc json = mapper.readValue(jsonBuilder.string (), ExpectedJsonDoc.class);
    ExpectedJsonDoc expected = new ExpectedJsonDoc ("x", "y", "z");
    assertThat(expected, is(json));
    
  }
  
  @Test
  public void testGetId () throws IndexLoaderException, JsonParseException, JsonMappingException, IOException {
    IndexDocumentHelper csvHelper = new CsvIndexDocumentHelper (new ArrayList<String> (){
      {add ("f1");add("f2");add("id");}      
    }, "id".split (","));
    
    XContentBuilder jsonBuilder = csvHelper.process (new String[] {"x", "y", "z"});      
    log.debug ("jsonBuilder.string(): "+jsonBuilder.string ());
    
    assertThat(csvHelper.getId (new String[] {"x", "y", "z"}), is("z"));
    
  }
  
  @Test
  public void testCreateMapping() throws IndexLoaderException, JsonParseException, JsonMappingException, IOException {
    IndexDocumentHelper csvHelper = new CsvIndexDocumentHelper (new ArrayList<String> (){
      {add ("f1");add("f2");add("id");}      
    }, "id".split (","));
        
    XContentBuilder jsonMapping = csvHelper.createMapping ("type_name");
    log.debug ("***jsonMapping.string() "+jsonMapping.string());
    assertEquals ("{\"_id\":{\"path\":\"id\"},\"properties\":{\"f1\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"ignore_malformed\":true},\"f2\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"ignore_malformed\":true},\"id\":{\"type\":\"string\",\"index\":\"not_analyzed\",\"ignore_malformed\":true}}}"
                  , jsonMapping.string ());
  }
  
  @Test(expected=NullPointerException.class) 
  public void testGetFieldIndex(){
    IndexDocumentHelper csvHelper = new CsvIndexDocumentHelper (new ArrayList<String> (){
      {add ("f1");add("f2");add("id");}      
    }, "id".split (","));
    assertEquals(0, csvHelper.getFieldIndex ("f1"));
    assertEquals(1, csvHelper.getFieldIndex ("f2"));
    assertEquals(2, csvHelper.getFieldIndex ("id"));
    csvHelper.getFieldIndex ("x");
  }
   
}
