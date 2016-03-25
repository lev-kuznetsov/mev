package edu.dfci.cccb.mev.web.test.histogram.controller;

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
import java.util.Arrays;
import java.util.List;

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
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramAnalysis;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramAnalysisBuilder;
import edu.dfci.cccb.mev.histogram.domain.contract.HistogramResult;
import edu.dfci.cccb.mev.histogram.domain.impl.SimpleHistogramAnalysis;
import edu.dfci.cccb.mev.histogram.domain.impl.SimpleHistogramResult;
import edu.dfci.cccb.mev.histogram.rest.configuration.HistogramAnalysisConfiguration;
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
                               HistogramAnalysisConfiguration.class
                               })
public class TestHistogramController {
  
  @Autowired WebApplicationContext applicationContext;
  private MockMvc mockMvc;
  private MockHttpSession mockHttpSession;    
  @Inject DatasetBuilder datasetBuilder;  
  private @Inject Workspace workspace;  
  private @Inject ObjectMapper jsonObjectMapper;
  Dataset dataset;  
  @Inject @Named("histogram.analysis.builder") Provider<HistogramAnalysisBuilder> builderProvider; 
  
  /* Sample json result
  {
  "breaks":[7,8,9,10,11,12,13,14,15,16,17,18,19],
  "counts":[4,6,7,24,14,17,31,74,27,14,15,1],
  "density":[0.0171,0.0256,0.0299,0.1026,0.0598,0.0726,0.1325,0.3162,0.1154,0.0598,0.0641,0.0043],
  "mids":[7.5,8.5,9.5,10.5,11.5,12.5,13.5,14.5,15.5,16.5,17.5,18.5]
  }  
  */
  Integer abreaks[] = {0,5,10,15,20,25,30,35,40}; List<Integer> breaks = Arrays.asList (abreaks);
  Integer acounts[] = {30,30,30,33,31,31,30,19}; List<Integer> counts= Arrays.asList (acounts);
  Double adensity[] = {0.0256,0.0256,0.0256,0.0282,0.0265,0.0265,0.0256,0.0162}; List<Double> density= Arrays.asList (adensity);
  Double amids[] = {2.5,7.5,12.5,17.5,22.5,27.5,32.5,37.5}; List<Double> mids= Arrays.asList (amids);
  HistogramResult result;
  
  String jsonResult = "{\"breaks\":[7,8,9,10,11,12,13,14,15,16,17,18,19],\"counts\":[4,6,7,24,14,17,31,74,27,14,15,1],\"density\":[0.0171,0.0256,0.0299,0.1026,0.0598,0.0726,0.1325,0.3162,0.1154,0.0598,0.0641,0.0043],\"mids\":[7.5,8.5,9.5,10.5,11.5,12.5,13.5,14.5,15.5,16.5,17.5,18.5]}";
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
    String analysisName = "histo_test";
    @SuppressWarnings ("unused")
    MvcResult mvcResult = this.mockMvc.perform(
                                               put(String.format("/dataset/%s/analyze/histogram/%s", dataset.name(), analysisName))
                                               .param ("format", "json")
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
      assertThat(analysisStatus.type (), is(HistogramAnalysis.ANALYSIS_TYPE));        
      assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));
      
      //Wait for analysis to complete
      Thread.sleep (2000L);
    
      HistogramAnalysis analysis = (HistogramAnalysis) dataset.analyses ().get (analysisName);
      log.debug("******* SimpleHistogramAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));      
      assertThat(analysis.name (), is(analysisName));      
      assertThat (analysis.result(), not(nullValue ()));
      
      HistogramResult result = jsonObjectMapper.readValue(jsonResult, SimpleHistogramResult.class);
      assertThat (analysis.result ().breaks (), is(result.breaks()));
      assertThat (analysis.result ().counts (), is(result.counts()));
      assertThat (analysis.result ().density(), is(result.density()));
      assertThat (analysis.result ().mids (), is(result.mids()));      
      
      MvcResult mvcResultGET = this.mockMvc.perform (get(String.format("/dataset/%s/analysis/%s", dataset.name(), analysisName))
                                                     .contentType (MediaType.APPLICATION_JSON)                                               
                                                     .accept("application/json")
                                                     .session (mockHttpSession)
                                                     .param ("format", "json")                                                     
                                                     )
     .andExpect (status ().isOk ())
     .andDo(print())
     .andReturn ();                                                
      HistogramAnalysis analysisFromGET = jsonObjectMapper.readValue (mvcResultGET.getResponse ().getContentAsString (), SimpleHistogramAnalysis.class); 
      assertThat (analysisFromGET.result ().breaks (), is(result.breaks()));
      assertThat (analysisFromGET.result ().counts (), is(result.counts()));
      assertThat (analysisFromGET.result ().density(), is(result.density()));
      assertThat (analysisFromGET.result ().mids (), is(result.mids()));      
      assertThat (analysisFromGET.name (), is(analysisName));
      assertThat(analysisFromGET.type (), is(HistogramAnalysis.ANALYSIS_TYPE));
      
  }

}
