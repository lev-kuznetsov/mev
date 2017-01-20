package edu.dfci.cccb.mev.web.test.dataset.rest.controller.selection;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
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
public class TestDatasetSelectionController {

  @Inject WebApplicationContext applicationContext;
  private MockMvc mockMvc;
  private @Inject ObjectMapper jsonObjectMapper;
  private MockHttpSession mockHttpSession;
  private Workspace workspace;
  private @Inject DatasetBuilder datasetBuilder;
  private Dataset mockDataset;
  
  
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
    RawInput rawInput = new MockTsvInput ("mock_set", "id\tsa\tsb\tsc\n" +
            "g1\t.1\t.2\t.3\n" +
            "g2\t.4\t.5\t.6");
    mockDataset = datasetBuilder.build (rawInput);
    log.debug("dataset.name: "+mockDataset.name ());
    workspace.put (mockDataset);
  }
  
  @Test 
  public void testGetSelections () throws Exception {
      
    //create selection
    final Properties properties = new Properties ();
    properties.put ("selectionDescription", "first mock selection");
    properties.put ("selectionColor", "#ff0000");    
    final List<String> keys = new ArrayList<String> ();
    keys.add ("sa");
    keys.add ("sc");    
    SimpleSelection selection = new SimpleSelection ("first", properties, keys);
    
    //put selectin
    Dimension column = mockDataset.dimension (Type.COLUMN);
    column.selections ().put (selection);
    
    @SuppressWarnings("unused")
    MvcResult mvcResult = this.mockMvc.perform(get("/dataset/mock_set/column/selections").param ("format", "json")
                                     .session (mockHttpSession)
                                     .accept("application/json"))            
     .andExpect (status ().isOk ())
     .andDo(print())
     .andReturn ();
  }

  @Test 
  public void testPutSelections () throws Exception {
      
    //create selection
    final Properties properties = new Properties ();
    properties.put ("selectionDescription", "first mock selection");
    properties.put ("selectionColor", "#ff0000");    
    final List<String> keys = new ArrayList<String> ();
    keys.add ("sa");
    keys.add ("sc");    
    
    //put selectin
    Dimension column = mockDataset.dimension (Type.COLUMN);    
    
    @SuppressWarnings("unused")
    MvcResult mvcResult = this.mockMvc.perform(
                                               put("/dataset/mock_set/column/selection/first")                                               
                                               .param ("selectionDescription", properties.getProperty ("selectionDescription"))
                                               .param ("selectionColor", properties.getProperty ("selectionColor"))
                                               .param ("keys", keys.toString ())
                                               .session (mockHttpSession)
                                               .accept("application/json")
                                               )            
     .andExpect (status ().isOk ())
     .andDo(print())
     .andReturn ();
    
    Selection actual = column.selections ().get ("first");    
    Selection expected = new SimpleSelection ("first", properties, keys);   
    assertThat(actual, equalTo (expected));
  }

  
  @Test 
  public void testPostSelections () throws Exception {
      
    //create selection
    final Properties properties = new Properties ();
    properties.put ("selectionDescription", "first mock selection");
    properties.put ("selectionColor", "#ff0000");
    final List<String> keys = new ArrayList<String> ();
    keys.add ("sa");
    keys.add ("sc");    
    SimpleSelection selection = new SimpleSelection ("first", properties, keys);
    
    //put selectin
    Dimension column = mockDataset.dimension (Type.COLUMN);
    column.selections ().put (selection);
    
    SimpleSelection selectionDto = new SimpleSelection ("second", properties, keys);
    String selectionJson = jsonObjectMapper.writeValueAsString (selectionDto);
    log.debug("selectionJson:"+selectionJson);
    
    @SuppressWarnings("unused")
    MvcResult mvcResult = this.mockMvc.perform(
                                               post("/dataset/mock_set/"+column.type ()+"/selection")
                                               .contentType (MediaType.APPLICATION_JSON)
                                               .content (selectionJson)
                                               .session (mockHttpSession)
                                               .accept("application/json")
                                               )                 
     .andDo(print())
     .andExpect (status ().isOk ())
     .andReturn ();
    
    Selection actual = column.selections ().get ("second");
    Selection expected = selectionDto;    
    assertThat(actual, equalTo (expected));
    
  }

}
