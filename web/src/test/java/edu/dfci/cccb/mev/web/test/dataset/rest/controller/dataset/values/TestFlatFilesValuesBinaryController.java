package edu.dfci.cccb.mev.web.test.dataset.rest.controller.dataset.values;


import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.ByteBuffer;

import javax.inject.Inject;

import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationProjectManagerConfiguration;
import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;
import lombok.extern.log4j.Log4j;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
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
        AnnotationProjectManagerConfiguration.class,
        PresetsRestConfiguration.class,
        ProbeAnnotationsPersistanceConfigTest.class})
public class TestFlatFilesValuesBinaryController {

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
  public void test () throws Exception {
    @SuppressWarnings("unused")
    MvcResult mvcResult = this.mockMvc.perform(get("/dataset/mock_set/data/values").param ("format", "binary64")
                                     .session (mockHttpSession)
                                     .accept("application/octet-stream"))            
     .andExpect (status ().isOk ())
     .andDo(print())
     .andReturn ();
     
     byte[] values = mvcResult.getResponse ().getContentAsByteArray ();
     ByteBuffer expected = ByteBuffer.wrap (values);
     int numOfCols = mockDataset.dimension (Type.COLUMN).keys ().size ();
     for(int irow = 0; irow < mockDataset.dimension (Type.ROW).keys ().size (); irow++){
       for(int icol = 0; icol < numOfCols; icol++){
         String row = mockDataset.dimension (Type.ROW).keys ().get(irow);
         String column = mockDataset.dimension (Type.COLUMN).keys ().get(icol);
         assertThat (expected.getDouble ((irow*numOfCols+icol)*Double.SIZE/Byte.SIZE), IsEqual.equalTo (mockDataset.values ().get (row, column))); ;
       }
     }
  }
  
  @Test
  public void testFloat () throws Exception {
    @SuppressWarnings("unused")
    MvcResult mvcResult = this.mockMvc.perform(get("/dataset/mock_set/data/values").param ("format", "binary")
                                     .session (mockHttpSession)
                                     .accept("application/octet-stream"))            
     .andExpect (status ().isOk ())
     .andDo(print())
     .andReturn ();
     
     byte[] values = mvcResult.getResponse ().getContentAsByteArray ();
     ByteBuffer expected = ByteBuffer.wrap (values);
     int numOfCols = mockDataset.dimension (Type.COLUMN).keys ().size ();
     for(int irow = 0; irow < mockDataset.dimension (Type.ROW).keys ().size (); irow++){
       for(int icol = 0; icol < numOfCols; icol++){
         String row = mockDataset.dimension (Type.ROW).keys ().get(irow);
         String column = mockDataset.dimension (Type.COLUMN).keys ().get(icol);
         assertThat (expected.getFloat ((irow*numOfCols+icol)*Float.SIZE/Byte.SIZE), IsEqual.equalTo ((float)mockDataset.values ().get (row, column))); ;
       }
     }
  }

}
