package edu.dfci.cccb.mev.test.genemad.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADResult;
import edu.dfci.cccb.mev.genemad.domain.impl.SimpleGeneMADResult;

@Log4j
public class TestSimpleGeneMADResult {

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
  
  private String jsonResult = "{"
          + "\"genes\":[\"Sell\",\"Gzmc\",\"Gzmb\",\"Card11\",\"Cars\",\"Syngr2\",\"Fut7\",\"St3gal2\",\"Actg1\",\"Ly6e\",\"Dmpk\",\"Pdcl3\",\"Ldha\",\"Chuk\",\"Aldoa\",\"Eef2\",\"Orc2\",\"Spp1\",\"Plek\",\"Cfl1\",\"Lars2\",\"Mtf2\",\"Myh9\",\"Tmsb4x\",\"Tln1\",\"Actb\",\"Pabpc1\",\"Eif4a1\",\"Tagln2\",\"Gnb2l1\",\"Top2a\",\"Lcp1\",\"Atp5b\",\"Eef1a1\",\"Pkm2\",\"Hnrnpa2b1\",\"Npm1\",\"Ppia\",\"Pgk1\"],"
          + "\"mad\":[2.5056,2.4463,2.0534,1.6605,1.5493,1.5493,1.49,1.4159,1.3121,1.2602,1.2009,1.1861,0.9637,0.9044,0.8821,0.771,0.7561,0.6894,0.6449,0.6301,0.6227,0.593,0.5782,0.5115,0.4448,0.4448,0.4225,0.4077,0.3484,0.3336,0.3188,0.2595,0.252,0.2076,0.2002,0.1705,0.1483,0.1334,0.0519]"
          + "}"; 
  
  @Before
  public void setup(){}
  
  @Test
  public void test () throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = mapper();
    GeneMADResult result = mapper.readValue (jsonResult, SimpleGeneMADResult.class);
    
    assertThat(result.genes(), is(result.genes()));
    assertThat(result.mad (), is(result.mad()));
    
  }

}
