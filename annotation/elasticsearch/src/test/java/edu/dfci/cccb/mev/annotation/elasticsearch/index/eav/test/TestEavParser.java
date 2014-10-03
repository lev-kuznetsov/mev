package edu.dfci.cccb.mev.annotation.elasticsearch.index.eav.test;

import static org.junit.Assert.*;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.annotation.elasticsearch.index.csv.test.AbstractTestWithElasticSearch;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract.IndexDocumentParserIterator;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.csv.EavParser;

@Log4j
public class TestEavParser extends AbstractTestWithElasticSearch{

  
  @ToString
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  @Accessors(fluent=true)
  private static class DummyMacsRecord{    
    @JsonProperty @Getter private String id;
    @JsonProperty @Getter private String case_id;
    @JsonProperty @Getter private String visit_id;
    @JsonProperty @Getter private String MACSCODE;
    @JsonProperty @Getter private String STATUS02;    
  }
  
  
  private EavParser parser;
  EavTestBag bag = new EavTestBag ();
  
  @Before
  public void setUp () throws Exception {
    parser = bag.getParser ();
  }

  @After
  public void tearDown () throws Exception {
    super.tearDown ();
    parser.close ();
  }

  @Test 
  public void testIterator () throws JsonParseException, JsonMappingException, IOException {
    int counter = 0;
    ObjectMapper mapper = new ObjectMapper ();
    IndexDocumentParserIterator it = parser.iterator ();
    while(it.hasNext ()){
      XContentBuilder jsonBuilder = it.next ();
      DummyMacsRecord macsRecord = mapper.readValue (jsonBuilder.string (), DummyMacsRecord.class);
      assertEquals(String.format ("100%s", counter), macsRecord.case_id ());
      assertFalse (macsRecord.case_id().equals (""));
      assertNotNull(macsRecord.case_id());
      assertFalse (macsRecord.visit_id().equals (""));
      assertNotNull(macsRecord.visit_id());
      assertEquals(String.format("%s!!!!%s", macsRecord.case_id(), macsRecord.visit_id ()), it.getId ());
      counter++;
    }
    assertEquals(5, counter);
        
  }

  @Test
  public void testGetMapping () throws IOException {
    XContentBuilder mapping = parser.getMapping ();
    log.debug (mapping.string ());
//    assertEquals ("{\"dynamic_templates\":[{\"dont_analyze\":{\"match\":\"*\",\"mapping\":{\"type\":\"string\",\"index\":\"no\",\"ignore_malformed\":true,\"norms\":{\"enabled\":false}}}}]}", mapping.string ());
    assertEquals ("{\"dynamic_templates\":[{\"dont_analyze\":{\"match\":\"*\",\"mapping\":{\"type\":\"string\",\"index\":\"no\",\"ignore_malformed\":true,\"norms\":{\"enabled\":false}}}}],\"properties\":{\"case_id\":{\"type\":\"String\",\"index\":\"not_analyzed\",\"norms\":{\"enabled\":false}},\"visit_id\":{\"type\":\"String\",\"index\":\"not_analyzed\",\"norms\":{\"enabled\":false}}}}", mapping.string ());
    
    CreateIndexRequestBuilder createIndexRequestBuilder = client.admin ().indices ()
            .prepareCreate (bag.getConfig ().indexName ())
            .addMapping (bag.getConfig ().typeName (), mapping);
    CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute ().actionGet ();
    assertTrue(createIndexResponse.isAcknowledged ());                                                                           
    
  }

}
