package edu.dfci.cccb.mev.test.histogram.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import lombok.extern.log4j.Log4j;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.r.RserveDatasetDeserializer;
import edu.dfci.cccb.mev.dataset.domain.r.RserveDatasetSerializer;
import edu.dfci.cccb.mev.dataset.domain.r.RserveDoubleDeserializer;
import edu.dfci.cccb.mev.dataset.domain.r.RserveDoubleSerializer;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramResult;
import edu.dfci.cccb.mev.histogram.domain.impl.SimpleHistogramResult;

@Log4j
public class TestSimpleHistogramResult {

  private Module rserveDatasetSerializationModule () {
    log.info ("Configuring Rserve json serialization");
    return new SimpleModule () {
      private static final long serialVersionUID = 1L;

      {
        addSerializer (Dataset.class, new RserveDatasetSerializer ());
        addSerializer (Double.class, new RserveDoubleSerializer ());

        addDeserializer (Double.class, new RserveDoubleDeserializer ());
        addDeserializer (Dataset.class, new RserveDatasetDeserializer ());
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
    return new ObjectMapper ().registerModules (rserveDatasetSerializationModule());
  }
  
  private String jsonResult = "{\n" + 
          "  \"breaks\":[0,20,40,60,80,100,120,140,160,180,200,220,240,260,280,300,320,340,360,380,400,420,440,460],\n" + 
          "  \"counts\":[47125,19715,19516,19575,19410,21335,23067,19363,19165,20184,19827,19418,20799,19650,19510,19669,19995,19568,19651,18641,12735,3143,55],\n" + 
          "  \"density\":[0.0053,0.0022,0.0022,0.0022,0.0022,0.0024,0.0026,0.0022,0.0022,0.0023,0.0022,0.0022,0.0024,0.0022,0.0022,0.0022,0.0023,0.0022,0.0022,0.0021,0.0014,0.0004,6.2342e-06],\n" + 
          "  \"mids\":[10,30,50,70,90,110,130,150,170,190,210,230,250,270,290,310,330,350,370,390,410,430,450]\n" + 
          "} "; 
  
  @Before
  public void setup(){}
  
  @Test
  public void test () throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = mapper();
    HistogramResult result = mapper.readValue (jsonResult, SimpleHistogramResult.class);
    Integer[] breaks = {0,20,40,60,80,100,120,140,160,180,200,220,240,260,280,300,320,340,360,380,400,420,440,460};
    Integer[] counts = {47125,19715,19516,19575,19410,21335,23067,19363,19165,20184,19827,19418,20799,19650,19510,19669,19995,19568,19651,18641,12735,3143,55};
    Double[] density = {0.0053,0.0022,0.0022,0.0022,0.0022,0.0024,0.0026,0.0022,0.0022,0.0023,0.0022,0.0022,0.0024,0.0022,0.0022,0.0022,0.0023,0.0022,0.0022,0.0021,0.0014,0.0004,6.2342e-06};
    Double[] mids = {10.,30.,50.,70.,90.,110.,130.,150.,170.,190.,210.,230.,250.,270.,290.,310.,330.,350.,370.,390.,410.,430.,450.};
    
    assertThat(result.breaks(), is(Arrays.asList (breaks)));
    assertThat(result.counts (), is(Arrays.asList (counts)));
    assertThat(result.density (), is(Arrays.asList (density)));
    assertThat(result.mids (), is(Arrays.asList (mids)));
     
  }

}
