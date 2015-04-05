package edu.dfci.cccb.mev.test.survival.domain;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
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
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalPlotEntry;
import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalResult;

@Log4j
public class TestSimpleSurvivalResult {

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
          "  \"n\" : 13,\n" + 
          "  \"n_event\" : 7,\n" + 
          "  \"coef\" : -0.2214,\n" + 
          "  \"exp_coef\" : 0.8014,\n" + 
          "  \"se_coef\" : 0.7829,\n" + 
          "  \"z\" : -0.2829,\n" + 
          "  \"pValue\" : 0.7773,\n" + 
          "  \"ci_lower\" : 0.1728,\n" + 
          "  \"ci_upper\" : 3.717,\n" + 
          "  \"logrank\" : {\n" + 
          "    \"score\" : 0.0803,\n" + 
          "    \"pValue\" : 0.7769\n" + 
          "  },\n" + 
          "  \"plot\" : {\n" + 
          "    \"control\" : [{\n" + 
          "        \"time\" : 1006,\n" + 
          "        \"haz\" : -0\n" + 
          "      },{\n" + 
          "        \"time\" : 1141,\n" + 
          "        \"haz\" : 0.1973\n" + 
          "      },{\"time\" : 1150, \"haz\" : \"Inf\"}],\n" + 
          "    \"experiment\" : [{\n" + 
          "        \"time\" : 1006,\n" + 
          "        \"haz\" : -0\n" + 
          "      },{\n" + 
          "        \"time\" : 1141,\n" + 
          "        \"haz\" : 0.1581\n" + 
          "      },{\"time\" : 1150, \"haz\" : \"Inf\"}]\n" + 
          "  }\n" + 
          "}"; 
  
  @Before
  public void setup(){}
  
  @Test
  public void test () throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = mapper();
    SimpleSurvivalResult result = mapper.readValue (jsonResult, SimpleSurvivalResult.class);
    assertThat(result.n(), is(13));
    assertThat(result.n_event (), is(7));
    assertThat(result.coef (), is(-0.2214));
    assertThat(result.exp_coef (), is(0.8014));
    assertThat(result.se_coef (), is(0.7829));
    assertThat(result.z (), is(-0.2829));
    assertThat(result.pValue (), is(0.7773));
    assertThat(result.ci_lower (), is(0.1728));
    assertThat(result.ci_upper (), is(3.717));
    assertThat(result.logrank ().score (), is(0.0803));
    assertThat(result.logrank ().pValue (), is(0.7769));
    assertThat(result.plot ().get ("control").size (), is(3));    
    SurvivalPlotEntry entry = result.plot ().get ("control").get(0);
    assertThat(entry.time (), is(1006));
    assertThat(entry.haz (), is(-0.));
    entry = result.plot ().get ("control").get(1);
    assertThat(entry.time (), is(1141));
    assertThat(entry.haz (), is(0.1973));
    
    entry = result.plot ().get ("experiment").get(0);
    assertThat(entry.time (), is(1006));
    assertThat(entry.haz (), is(-0.));
    entry = result.plot ().get ("experiment").get(1);
    assertThat(entry.time (), is(1141));
    assertThat(entry.haz (), is(0.1581));
    
  }

}
