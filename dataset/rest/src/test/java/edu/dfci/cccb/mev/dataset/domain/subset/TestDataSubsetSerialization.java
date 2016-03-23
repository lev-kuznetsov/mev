package edu.dfci.cccb.mev.dataset.domain.subset;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import lombok.extern.log4j.Log4j;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.DimensionTypeJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.SimpleDatasetJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.SimpleDimensionJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.SimpleSelectionJsonSerializer;

@Log4j
public class TestDataSubsetSerialization {

  private Module simpleDatasetSerializationModule () {
    log.info ("Configuring Simple json serialization");
    return new SimpleModule () {
      private static final long serialVersionUID = 1L;

      {
        addSerializer (Dataset.class, new SimpleDatasetJsonSerializer ());
        addSerializer (SimpleDimension.class, new SimpleDimensionJsonSerializer ());
        addSerializer (Dimension.Type.class, new DimensionTypeJsonSerializer  ());
        addSerializer (SimpleSelection.class, new SimpleSelectionJsonSerializer ());
        
      }

      @Override
      public <T> SimpleModule addSerializer (Class<? extends T> type, JsonSerializer<T> ser) {
        return super.addSerializer (type, ser);
      }
      @Override
      public <T> SimpleModule addDeserializer (Class<T> type, JsonDeserializer<? extends T> deser) {
        return super.addDeserializer (type, deser);
      }
    };
  }

  
  private ObjectMapper mapper () {
    return new ObjectMapper ().registerModules (simpleDatasetSerializationModule());
  }
  
  private SimpleDatasetBuilder builder;
  private Dataset dataset;
  private Dataset subset;
  private Dataset expectedSubset;
  
  @Rule
  public ExpectedException thrown= ExpectedException.none();
  
  @Before
  public void initializeBuilder () throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException {
    builder = new SimpleDatasetBuilder ();
    builder.setParserFactories (asList (new SuperCsvParserFactory ()));
    builder.setValueStoreBuilder (new MapBackedValueStoreBuilder ());
    dataset = builder.build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\tsd\n" +
            "g1\t1.1\t1.2\t1.3\t1.4\n" +
            "g2\t2.1\t2.2\t2.3\t2.4\n" +
            "g3\t3.1\t3.2\t3.3\t3.4\n" + 
            "g4\t4.1\t4.2\t4.3\t4.4\n" 
            ));
    subset = dataset.subset ("subset1", 
      new ArrayList <String>(){
        {add("sb"); add("sc");}
      }, 
      new ArrayList <String>(){
        {add("g2"); add("g3");}
      });
    builder = new SimpleDatasetBuilder ();
    builder.setParserFactories (asList (new SuperCsvParserFactory ()));
    builder.setValueStoreBuilder (new MapBackedValueStoreBuilder ());
    expectedSubset = builder.build (new MockTsvInput ("subset1", "id\tsb\tsc\n" +            
      "g2\t2.2\t2.3\n" +
      "g3\t3.2\t3.3\n"            
      ));
  }
  
  @Before
  public void setup(){}
  
  @Test
  public void test () throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = mapper();
    
    String json = mapper.writeValueAsString (subset);
    log.debug (json);    
    String expectedJson = mapper.writeValueAsString(expectedSubset);
    log.debug (expectedJson);
    
    JsonNode nodeResult = mapper.readTree (json);
    JsonNode nodeExpectedResult = mapper.readTree (expectedJson);
    assertThat(json, new IsEqual<String> (expectedJson));
    assertTrue(nodeResult.equals (nodeExpectedResult));
    
  }
}
