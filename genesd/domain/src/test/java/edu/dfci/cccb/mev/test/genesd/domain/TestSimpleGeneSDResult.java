package edu.dfci.cccb.mev.test.genesd.domain;

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
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDResult;
import edu.dfci.cccb.mev.genesd.domain.impl.SimpleGeneSDResult;

@Log4j
public class TestSimpleGeneSDResult {

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
          + "\"genes\":[\"Sell\",\"Gzmc\",\"Gzmb\",\"Pdcl3\",\"Fut7\",\"Card11\",\"Lars2\",\"Syngr2\",\"Chuk\",\"Actg1\",\"Cars\",\"St3gal2\",\"Ly6e\",\"Dmpk\",\"Ldha\",\"Plek\",\"Mtf2\",\"Cfl1\",\"Aldoa\",\"Tmsb4x\",\"Spp1\",\"Orc2\",\"Eef2\",\"Actb\",\"Tln1\",\"Lcp1\",\"Tagln2\",\"Atp5b\",\"Myh9\",\"Eif4a1\",\"Npm1\",\"Eef1a1\",\"Pabpc1\",\"Ppia\",\"Top2a\",\"Gnb2l1\",\"Pkm2\",\"Hnrnpa2b1\",\"Pgk1\"],"
          + "\"sd\":[1.9458,1.8781,1.6143,1.439,1.3118,1.2852,1.2713,1.2454,1.2362,1.1808,1.1056,1.094,0.9577,0.9406,0.8323,0.7701,0.7468,0.6949,0.6706,0.6558,0.6225,0.6022,0.5536,0.536,0.5085,0.5046,0.4913,0.4662,0.4496,0.3662,0.356,0.3529,0.3379,0.3034,0.2906,0.2757,0.2502,0.2345,0.0543]}"; 
  
  @Before
  public void setup(){}
  
  @Test
  public void test () throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = mapper();
    GeneSDResult result = mapper.readValue (jsonResult, SimpleGeneSDResult.class);
    
    assertThat(result.genes(), is(result.genes()));
    assertThat(result.sd (), is(result.sd()));
    
  }

}
