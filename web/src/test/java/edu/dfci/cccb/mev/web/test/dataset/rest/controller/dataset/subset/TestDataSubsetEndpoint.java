package edu.dfci.cccb.mev.web.test.dataset.rest.controller.dataset.subset;

import static org.junit.Assert.*;
import static java.util.Arrays.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationProjectManagerConfiguration;
import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;
import lombok.extern.log4j.Log4j;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.dataset.rest.controllers.DataController.SubsetRequest;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;
import edu.dfci.cccb.mev.web.configuration.DispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.PersistenceConfiguration;
import edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes={DispatcherConfiguration.class, 
                               PersistenceConfiguration.class, 
                               ContainerConfigurations.class, 
                               DatasetRestConfiguration.class,
        AnnotationProjectManagerConfiguration.class,
                               PresetsRestConfiguration.class,
                               ProbeAnnotationsPersistanceConfigTest.class})
public class TestDataSubsetEndpoint {

  @Inject WebApplicationContext applicationContext;
  private MockMvc mockMvc;
  private @Inject ObjectMapper jsonObjectMapper;
  private MockHttpSession mockHttpSession;
  private Workspace workspace;
  private @Inject DatasetBuilder datasetBuilder;
  private Dataset dataset;
  private Dataset expectedSubset;
  
  @Before
  public void setup() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException{
    //create web applicatin context
    mockMvc = MockMvcBuilders.webAppContextSetup (applicationContext).build ();
    //create a new session object
    mockHttpSession = new MockHttpSession ();
    //create a new request
    MockHttpServletRequest mockHttpRequest = new MockHttpServletRequest ();
    //assign session to request
    mockHttpRequest.setSession (mockHttpSession);
    //bind current thread context to the request object
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpRequest));
    //touch the workspace bean to create new workspace incurrent session
    workspace = applicationContext.getBean(Workspace.class);
    //create dummy dataset
    RawInput rawInput = new MockTsvInput ("mockdataset", "id\tsa\tsb\tsc\n" +
            "g1\t.1\t.2\t.3\n" +
            "g2\t.4\t.5\t.6");
    dataset = datasetBuilder.build (rawInput);
    log.debug("dataset.name: "+dataset.name ());
    workspace.put (dataset);
    
    SimpleDatasetBuilder builder = new SimpleDatasetBuilder ();
    builder.setParserFactories (asList (new SuperCsvParserFactory ()));
    builder.setValueStoreBuilder (new MapBackedValueStoreBuilder ());
    expectedSubset = builder.build (new MockTsvInput ("subset1", "id\tsa\tsc\n" +            
      "g2\t.4\t.6\n" 
      ));
  }
  
  @Test
  public void test () throws Exception {
    
    SubsetRequest subsetRequest = new SubsetRequest ("subset1", asList ("sa", "sc"), asList("g2"));
    String subsetRequestJson = jsonObjectMapper.writeValueAsString (subsetRequest);
    log.debug(String.format("subsetRequestJson: %s", subsetRequestJson));
    @SuppressWarnings("unused")
    MvcResult mvcResult = this.mockMvc.perform(
                                               post(String.format("/dataset/%s/data/subset", dataset.name()))
                                               .param ("format", "json")
                                               .contentType (MediaType.APPLICATION_JSON)
                                               .content (subsetRequestJson)
                                               .session (mockHttpSession)
                                               .accept("application/json")
                                               )                 
     .andDo(print())
     .andExpect (status ().isOk ())
     .andReturn ();
    
    String json = mvcResult.getResponse ().getContentAsString ();
    log.debug (json);    
    String expectedJson = jsonObjectMapper.writeValueAsString(expectedSubset);
    log.debug (expectedJson);
    
    JsonNode nodeResult = jsonObjectMapper.readTree (json);
    JsonNode nodeExpectedResult = jsonObjectMapper.readTree (expectedJson);    
    assertTrue(nodeResult.equals (nodeExpectedResult));
  }

}
