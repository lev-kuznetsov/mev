package edu.dfci.cccb.mev.web.test.genesd.controller;

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
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDAnalysis;
import edu.dfci.cccb.mev.genesd.domain.contract.GeneSDResult;
import edu.dfci.cccb.mev.genesd.domain.impl.RserveGeneSDAnalysisBuilder;
import edu.dfci.cccb.mev.genesd.domain.impl.SimpleGeneSDAnalysis;
import edu.dfci.cccb.mev.genesd.domain.impl.SimpleGeneSDResult;
import edu.dfci.cccb.mev.genesd.rest.configuration.GeneSDAnalysisConfiguration;
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
                               GeneSDAnalysisConfiguration.class
                               })
public class TestGeneSDController {
  
  @Autowired WebApplicationContext applicationContext;
  private MockMvc mockMvc;
  private MockHttpSession mockHttpSession;    
  @Inject DatasetBuilder datasetBuilder;  
  private @Inject Workspace workspace;  
  private @Inject ObjectMapper jsonObjectMapper;
  Dataset dataset;  
  @Inject @Named("genesd.analysis.builder") Provider<RserveGeneSDAnalysisBuilder> builderProvider; 
  
  GeneSDResult result;  
  private String jsonResult = "{"
          + "\"genes\":[\"Sell\",\"Gzmc\",\"Gzmb\",\"Pdcl3\",\"Fut7\",\"Card11\",\"Lars2\",\"Syngr2\",\"Chuk\",\"Actg1\",\"Cars\",\"St3gal2\",\"Ly6e\",\"Dmpk\",\"Ldha\",\"Plek\",\"Mtf2\",\"Cfl1\",\"Aldoa\",\"Tmsb4x\",\"Spp1\",\"Orc2\",\"Eef2\",\"Actb\",\"Tln1\",\"Lcp1\",\"Tagln2\",\"Atp5b\",\"Myh9\",\"Eif4a1\",\"Npm1\",\"Eef1a1\",\"Pabpc1\",\"Ppia\",\"Top2a\",\"Gnb2l1\",\"Pkm2\",\"Hnrnpa2b1\",\"Pgk1\"],"
          + "\"sd\":[1.9458,1.8781,1.6143,1.439,1.3118,1.2852,1.2713,1.2454,1.2362,1.1808,1.1056,1.094,0.9577,0.9406,0.8323,0.7701,0.7468,0.6949,0.6706,0.6558,0.6225,0.6022,0.5536,0.536,0.5085,0.5046,0.4913,0.4662,0.4496,0.3662,0.356,0.3529,0.3379,0.3034,0.2906,0.2757,0.2502,0.2345,0.0543]}"; 
  
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
    MvcResult mvcResult = this.mockMvc.perform(
                                               put(String.format("/dataset/%s/analyze/genesd/%s", dataset.name(), analysisName))
                                               .param ("format", "json")
                                               .contentType (MediaType.APPLICATION_JSON)                                               
                                               .accept("application/json")
                                               .session (mockHttpSession)
                                               )            
     .andExpect (status ().isOk ())
     .andDo(print())
     .andReturn ();
    
      GeneSDAnalysis analysis = (GeneSDAnalysis) dataset.analyses ().get (analysisName);
      log.debug("******* SimpleGeneSDAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));      
      assertThat(analysis.name (), is(analysisName));      
      assertThat (analysis.result(), not(nullValue ()));
      
      GeneSDResult result = jsonObjectMapper.readValue(jsonResult, SimpleGeneSDResult.class);
      assertThat (analysis.result ().genes (), is(result.genes()));
      assertThat (analysis.result ().sd(), is(result.sd()));
      assertThat (analysis.type(), is(GeneSDAnalysis.ANALYSIS_TYPE));
      
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
          
          GeneSDAnalysis analysisFromJson = jsonObjectMapper.readValue(mvcResultGET.getResponse ().getContentAsString (), SimpleGeneSDAnalysis.class);
          assertThat (analysis.result ().genes (), is(result.genes()));
          assertThat (analysis.result ().sd(), is(result.sd()));
          assertThat (analysis.type(), is(GeneSDAnalysis.ANALYSIS_TYPE));          
          assertThat (analysisFromJson.name(), is(analysisName));
      
  }
  
  @Test @Ignore
  public void testAsync () throws Exception {
    String analysisName = "genesd_test";
    @SuppressWarnings ("unused")
    MvcResult mvcResult = this.mockMvc.perform(
                                               put(String.format("/dataset/%s/analyze/genesd/%s", dataset.name(), analysisName))
                                               .param("format", "json")
                                               .contentType (MediaType.APPLICATION_JSON)                                               
                                               .accept("application/json")
                                               .session (mockHttpSession)
            )            
            .andExpect (status ().isOk ())
            .andDo(print())
            .andReturn ();
    
    //The first put will generate an AnalysisStatus object with "IN_PROGRESS" status
    Analysis analysisStatus = dataset.analyses ().get (analysisName);
    log.debug("******* AnalysisStatus:\n"+ jsonObjectMapper.writeValueAsString (analysisStatus));      
    assertThat(analysisStatus.name (), is(analysisName));        
    assertThat(analysisStatus.type (), is(GeneSDAnalysis.ANALYSIS_TYPE));        
    assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));
    
    //Wait for analysis to complete
    Thread.sleep (2000L);

    GeneSDAnalysis analysis = (GeneSDAnalysis) dataset.analyses ().get (analysisName);
    log.debug("******* SimpleGeneSDAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));      
    assertThat(analysis.name (), is(analysisName));      
    assertThat (analysis.result(), not(nullValue ()));
    
    GeneSDResult result = jsonObjectMapper.readValue(jsonResult, SimpleGeneSDResult.class);
    assertThat (analysis.result ().genes (), is(result.genes()));
    assertThat (analysis.result ().sd(), is(result.sd()));
    assertThat (analysis.type(), is(GeneSDAnalysis.ANALYSIS_TYPE));
    
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
    
    GeneSDAnalysis analysisFromJson = jsonObjectMapper.readValue(mvcResultGET.getResponse ().getContentAsString (), SimpleGeneSDAnalysis.class);
    assertThat (analysis.result ().genes (), is(result.genes()));
    assertThat (analysis.result ().sd(), is(result.sd()));
    assertThat (analysis.type(), is(GeneSDAnalysis.ANALYSIS_TYPE));          
    assertThat (analysisFromJson.name(), is(analysisName));
    
  }
}
