package edu.dfci.cccb.mev.web.test.genemad.controller;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import lombok.extern.log4j.Log4j;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADAnalysis;
import edu.dfci.cccb.mev.genemad.domain.contract.GeneMADResult;
import edu.dfci.cccb.mev.genemad.domain.impl.RserveGeneMADAnalysisBuilder;
import edu.dfci.cccb.mev.genemad.domain.impl.SimpleGeneMADAnalysis;
import edu.dfci.cccb.mev.genemad.domain.impl.SimpleGeneMADResult;
import edu.dfci.cccb.mev.genemad.rest.configuration.GeneMADAnalysisConfiguration;
import edu.dfci.cccb.mev.web.configuration.DispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.PersistenceConfiguration;
import edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations;

@Log4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DispatcherConfiguration.class, 
                               PersistenceConfiguration.class, 
                               ContainerConfigurations.class, 
                               DatasetRestConfiguration.class,
                               RDispatcherConfiguration.class,
                               GeneMADAnalysisConfiguration.class
                               })
public class TestGeneMADController {
  
  @Autowired WebApplicationContext applicationContext;
  private MockMvc mockMvc;
  private MockHttpSession mockHttpSession;    
  @Inject DatasetBuilder datasetBuilder;  
  private @Inject Workspace workspace;  
  private @Inject ObjectMapper jsonObjectMapper;
  Dataset dataset;  
  @Inject @Named("genemad.analysis.builder") Provider<RserveGeneMADAnalysisBuilder> builderProvider; 
  
  GeneMADResult result;  
  private String jsonResult = "{"
          + "\"genes\":[\"Sell\",\"Gzmc\",\"Gzmb\",\"Card11\",\"Cars\",\"Syngr2\",\"Fut7\",\"St3gal2\",\"Actg1\",\"Ly6e\",\"Dmpk\",\"Pdcl3\",\"Ldha\",\"Chuk\",\"Aldoa\",\"Eef2\",\"Orc2\",\"Spp1\",\"Plek\",\"Cfl1\",\"Lars2\",\"Mtf2\",\"Myh9\",\"Tmsb4x\",\"Tln1\",\"Actb\",\"Pabpc1\",\"Eif4a1\",\"Tagln2\",\"Gnb2l1\",\"Top2a\",\"Lcp1\",\"Atp5b\",\"Eef1a1\",\"Pkm2\",\"Hnrnpa2b1\",\"Npm1\",\"Ppia\",\"Pgk1\"],"
          + "\"mad\":[2.5056,2.4463,2.0534,1.6605,1.5493,1.5493,1.49,1.4159,1.3121,1.2602,1.2009,1.1861,0.9637,0.9044,0.8821,0.771,0.7561,0.6894,0.6449,0.6301,0.6227,0.593,0.5782,0.5115,0.4448,0.4448,0.4225,0.4077,0.3484,0.3336,0.3188,0.2595,0.252,0.2076,0.2002,0.1705,0.1483,0.1334,0.0519]"
          + "}"; 
  
  @Before
  public void setup() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException, IOException{
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    MockHttpServletRequest request = new MockHttpServletRequest();
    mockHttpSession = new MockHttpSession();
    request.setSession(mockHttpSession);
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    applicationContext.getBean(Workspace.class);
    
    URL urlData = this.getClass ().getResource ("/mouse_test_data.tsv");
    RawInput input = new UrlTsvInput (urlData);    
    dataset = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
              .setValueStoreBuilder (new FlatFileValueStoreBuilder ())              
              .build (input);
    workspace.put (dataset);
    
  }
  
  //This test only works if local rserve is running
  @Test @Ignore
  public void test () throws Exception {
    String analysisName = "genesd_test";
    @SuppressWarnings ("unused")
    MvcResult mvcResultPUT = this.mockMvc.perform(
                                               put(String.format("/dataset/%s/analyze/genemad/%s", dataset.name(), analysisName))            
                                               .contentType (MediaType.APPLICATION_JSON)                                               
                                               .accept("application/json")
                                               .session (mockHttpSession)
                                               )        
     .andExpect (status ().isOk ())
     .andDo(print())
     .andReturn ();
    
      
    
    
      GeneMADAnalysis analysis = (GeneMADAnalysis) dataset.analyses ().get (analysisName);
      log.debug("******* GeneMADAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));      
      assertThat(analysis.name (), is(analysisName));      
      assertThat (analysis.result(), not(nullValue ()));
      
      GeneMADResult result = jsonObjectMapper.readValue(jsonResult, SimpleGeneMADResult.class);
      assertThat (analysis.result ().genes (), is(result.genes()));
      assertThat (analysis.result ().mad(), is(result.mad()));
      assertThat (analysis.type(), is(GeneMADAnalysis.ANALYSIS_TYPE));
    
      MvcResult mvcResultGET = this.mockMvc.perform(
                                              get(String.format("/dataset/%s/analysis/%s", dataset.name(), analysisName))            
                                              .contentType (MediaType.APPLICATION_JSON)                                               
                                              .accept("application/json")
                                              .session (mockHttpSession)
                                              .param ("format", "json")
                                              )        
    .andExpect (status ().isOk ())
    .andDo(print())
    .andReturn ();
    log.debug(String.format("mvcResultGET content: %s", mvcResultGET.getResponse ().getContentAsString ()));
    
    GeneMADAnalysis analysisFromJson = jsonObjectMapper.readValue(mvcResultGET.getResponse ().getContentAsString (), SimpleGeneMADAnalysis.class);
    assertThat (analysisFromJson.result ().genes (), is(result.genes()));
    assertThat (analysisFromJson.result ().mad(), is(result.mad()));
    assertThat (analysisFromJson.type(), is(GeneMADAnalysis.ANALYSIS_TYPE));
    assertThat (analysisFromJson.name(), is(analysisName));
  }

  @Test @Ignore
  public void testAsync () throws Exception {
        
    String analysisName = "genemad_test";
    @SuppressWarnings ("unused")
    MvcResult mvcResultPUT = this.mockMvc.perform(
                                                  put(String.format("/dataset/%s/analyze/genemad/%s", dataset.name(), analysisName))
                                                  .param ("format", "json")
                                                  .contentType (MediaType.APPLICATION_JSON)                                                  
                                                  .accept(MediaType.APPLICATION_JSON)
                                                  .session (mockHttpSession)
            )        
            .andExpect (status ().isOk ())
            .andDo(print())
            .andReturn ();
    
    //The first put will generate an AnalysisStatus object with "IN_PROGRESS" status
    Analysis analysisStatus = dataset.analyses ().get (analysisName);
    log.debug("******* AnalysisStatus:\n"+ jsonObjectMapper.writeValueAsString (analysisStatus));      
    assertThat(analysisStatus.name (), is(analysisName));        
    assertThat(analysisStatus.type (), is(GeneMADAnalysis.ANALYSIS_TYPE));        
    assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));        
     
    //Wait for analysis to complete
    Thread.sleep (2000L);
    
    //The second get should return the actual analysis
    GeneMADAnalysis analysis = (GeneMADAnalysis) dataset.analyses ().get (analysisName);
    log.debug("******* GeneMADAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));      
    assertThat(analysis.name (), is(analysisName));      
    assertThat (analysis.result(), not(nullValue ()));
    
    GeneMADResult result = jsonObjectMapper.readValue(jsonResult, SimpleGeneMADResult.class);
    assertThat (analysis.result ().genes (), is(result.genes()));
    assertThat (analysis.result ().mad(), is(result.mad()));
    assertThat (analysis.type(), is(GeneMADAnalysis.ANALYSIS_TYPE));
    
    MvcResult mvcResultGET = this.mockMvc.perform(
                                                  get(String.format("/dataset/%s/analysis/%s", dataset.name(), analysisName))
                                                  .param ("format", "json")
                                                  .contentType (MediaType.APPLICATION_JSON)                                               
                                                  .accept("application/json")
                                                  .session (mockHttpSession)
                                                  .param ("format", "json")
            )        
            .andExpect (status ().isOk ())
            .andDo(print())
            .andReturn ();
    log.debug(String.format("mvcResultGET content: %s", mvcResultGET.getResponse ().getContentAsString ()));
    
    GeneMADAnalysis analysisFromJson = jsonObjectMapper.readValue(mvcResultGET.getResponse ().getContentAsString (), SimpleGeneMADAnalysis.class);
    assertThat (analysisFromJson.result ().genes (), is(result.genes()));
    assertThat (analysisFromJson.result ().mad(), is(result.mad()));
    assertThat (analysisFromJson.type(), is(GeneMADAnalysis.ANALYSIS_TYPE));
    assertThat (analysisFromJson.name(), is(analysisName));
  }
}
