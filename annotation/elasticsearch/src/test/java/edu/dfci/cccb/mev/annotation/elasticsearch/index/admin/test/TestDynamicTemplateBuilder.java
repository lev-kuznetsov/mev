package edu.dfci.cccb.mev.annotation.elasticsearch.index.admin.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder_NoIndex;

public class TestDynamicTemplateBuilder {

  @Test
  public void test () throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    DynamicTemplateBuilder builder = new DynamicTemplateBuilder_NoIndex ();
    
    List<Map<String, Object>> tempalte = builder.build (); 
    String actual = mapper.writeValueAsString (tempalte);
    assertEquals ("[{\"dont_analyze\":{\"match\":\"*\",\"mapping\":{\"type\":\"string\",\"index\":\"no\",\"ignore_malformed\":true,\"norms\":{\"enabled\":false}}}}]", actual);
  }

}
