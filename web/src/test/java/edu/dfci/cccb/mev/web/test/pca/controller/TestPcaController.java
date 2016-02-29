package edu.dfci.cccb.mev.web.test.pca.controller;

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
import edu.dfci.cccb.mev.pca.domain.Pca;
import edu.dfci.cccb.mev.pca.domain.PcaBuilder;
import edu.dfci.cccb.mev.pca.rest.configuration.PcaConfiguration;
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
                               PcaConfiguration.class
                               })
public class TestPcaController {
  
  @Autowired WebApplicationContext applicationContext;
  private MockMvc mockMvc;
  private MockHttpSession mockHttpSession;    
  @Inject DatasetBuilder datasetBuilder;  
  private @Inject Workspace workspace;  
  private @Inject ObjectMapper jsonObjectMapper;
  Dataset dataset;  
  @Inject @Named("pca.analysis.builder") Provider<PcaBuilder> builderProvider; 
  
   
  private String jsonResult = " {\"name\":\"pca_test\",\"type\":\"pca\",\"status\":\"SUCCESS\",\"error\":null,\"sdev\":[5.1805,1.6014,1.27,0.9503,0.5925,4.579E-15],\"x\":{\"A\":{\"PC1\":-5.9558,\"PC2\":1.1324,\"PC3\":1.055},\"B\":{\"PC1\":-2.3418,\"PC2\":-2.8169,\"PC3\":-1.0186},\"C\":{\"PC1\":-5.4606,\"PC2\":0.8236,\"PC3\":-0.5208},\"D\":{\"PC1\":4.2453,\"PC2\":0.1323,\"PC3\":0.7853},\"E\":{\"PC1\":4.4878,\"PC2\":-0.7856,\"PC3\":1.4276},\"F\":{\"PC1\":5.0251,\"PC2\":1.5142,\"PC3\":-1.7286}}}"; 
 
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

  @Test @Ignore 
  public void testAsync () throws Exception {
        
    String analysisName = "pca_test";
    @SuppressWarnings ("unused")
    MvcResult mvcResultPUT = this.mockMvc.perform(
                                                  put(String.format("/dataset/%s/analyze/pca/%s", dataset.name(), analysisName))
                                                  .param ("format", "json")
                                                  .contentType (MediaType.APPLICATION_JSON)                                                  
                                                  .accept(MediaType.APPLICATION_JSON)
                                                  .session (mockHttpSession)
                                                  .content(String.format("{\"name\": \"%s\"}", analysisName))
            )        
            .andExpect (status ().isOk ())
            .andDo(print())
            .andReturn ();
    
    //The first put will generate an AnalysisStatus object with "IN_PROGRESS" status
    Analysis analysisStatus = dataset.analyses ().get (analysisName);
    log.debug("******* AnalysisStatus:\n"+ jsonObjectMapper.writeValueAsString (analysisStatus));      
    assertThat(analysisStatus.name (), is(analysisName));        
    assertThat(analysisStatus.type (), is("pca"));        
    assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));        
     
    //Wait for analysis to complete
    Thread.sleep (2000L);
    
    //The second get should return the actual analysis
    Pca analysis = (Pca) dataset.analyses ().get (analysisName);
    log.debug("******* PcaAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));      
    assertThat(analysis.name (), is(analysisName));      
    assertThat (analysis.x(), not(nullValue ()));
    assertThat (analysis.sdev(), not(nullValue ()));
    
    Pca result = jsonObjectMapper.readValue(jsonResult, Pca.class);
    assertThat (analysis.type(), is(result.type()));
    
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
    
    Pca analysisFromJson = jsonObjectMapper.readValue(mvcResultGET.getResponse ().getContentAsString (), Pca.class);
//    assertThat (analysisFromJson.result ().genes (), is(result.genes()));
//    assertThat (analysisFromJson.result ().mad(), is(result.mad()));
//    assertThat (analysisFromJson.type(), is("pca"));
    assertThat (analysisFromJson.name(), is(analysisName));
  }

  @Test  @Ignore
  public void testAsyncDataSubset () throws Exception {
	  
	  String analysisName = "pca_test";
	  @SuppressWarnings ("unused")
	  MvcResult mvcResultPUT = this.mockMvc.perform(
			  put(String.format("/dataset/%s/analyze/pca/%s", dataset.name(), analysisName))
			  .param ("format", "json")
			  .contentType (MediaType.APPLICATION_JSON)                                                  
			  .accept(MediaType.APPLICATION_JSON)
			  .session (mockHttpSession)
			  .content(String.format("{\"name\": \"%s\", \"sampleList\":[\"D\",\"E\",\"F\"]}", analysisName))
			  )        
			  .andExpect (status ().isOk ())
			  .andDo(print())
			  .andReturn ();
	  
	  //The first put will generate an AnalysisStatus object with "IN_PROGRESS" status
	  Analysis analysisStatus = dataset.analyses ().get (analysisName);
	  log.debug("******* AnalysisStatus:\n"+ jsonObjectMapper.writeValueAsString (analysisStatus));      
	  assertThat(analysisStatus.name (), is(analysisName));        
	  assertThat(analysisStatus.type (), is("pca"));        
	  assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));        
	  
	  //Wait for analysis to complete
	  Thread.sleep (2000L);
	  
	  //The second get should return the actual analysis
	  Pca analysis = (Pca) dataset.analyses ().get (analysisName);
	  log.debug("******* PcaAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));      
	  assertThat(analysis.name (), is(analysisName));      
	  assertThat (analysis.x(), not(nullValue ()));
	  assertThat (analysis.sdev(), not(nullValue ()));
	  
	  Pca result = jsonObjectMapper.readValue(jsonResult, Pca.class);
	  assertThat (analysis.type(), is(result.type()));
	  
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
	  
	  Pca analysisFromJson = jsonObjectMapper.readValue(mvcResultGET.getResponse ().getContentAsString (), Pca.class);
//    assertThat (analysisFromJson.result ().genes (), is(result.genes()));
//    assertThat (analysisFromJson.result ().mad(), is(result.mad()));
//    assertThat (analysisFromJson.type(), is("pca"));
	  assertThat (analysisFromJson.name(), is(analysisName));
  }
}
