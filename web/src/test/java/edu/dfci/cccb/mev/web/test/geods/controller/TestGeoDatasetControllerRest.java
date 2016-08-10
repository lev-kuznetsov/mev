package edu.dfci.cccb.mev.web.test.geods.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationProjectManagerConfiguration;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;
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

import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.geods.rest.controllers.GeoDatasetController.GeoImportDto;
import edu.dfci.cccb.mev.test.geods.rest.configuration.GeoDatasetsConfigurationTEST;
import edu.dfci.cccb.mev.web.configuration.DispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations;

@Log4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DispatcherConfiguration.class,
                               ContainerConfigurations.class,
                               GeoDatasetsConfigurationTEST.class,
                               DatasetRestConfiguration.class,
                               AnnotationProjectManagerConfiguration.class,
                               ProbeAnnotationsPersistanceConfigTest.class,
                               PresetsRestConfiguration.class
                               })
public class TestGeoDatasetControllerRest {

  private @Inject WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private MockHttpSession mockHttpSession;
  private @Inject ObjectMapper jsonObjectMapper;
  
  @Before
  public void setUp(){
    mockMvc = MockMvcBuilders.webAppContextSetup (webApplicationContext).build ();
    //create a new session object
    mockHttpSession = new MockHttpSession ();
    //create a new request
    MockHttpServletRequest mockHttpRequest = new MockHttpServletRequest ();
    //assign session to request
    mockHttpRequest.setSession (mockHttpSession);
    //bind current thread context to the request object
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpRequest));
    
  }
  
  @Test 
  public void test () throws Exception {
    GeoImportDto dto = new GeoImportDto("4092", "570"
//                                        , new ArrayList<String> (){
//      private static final long serialVersionUID = 1L;
//      {
//        add("aaa");
//        add("bbb");
//        add("ccc");
//      }
//    }
                                        , new LinkedHashMap<String, String> (){
      private static final long serialVersionUID = 1L;
      {
        put ("a", "aaa");
        put ("b", "bbb");
        put ("c", "ccc");
      }
      
    }
    , "ftp://ftp.ncbi.nlm.nih.gov/geo/datasets/GDS4nnn/GDS4092/");
    String dtoJson = jsonObjectMapper.writeValueAsString (dto);
    log.debug("goeDtoJson:"+dtoJson);
    
    
    @SuppressWarnings("unused")
    MvcResult mvcResult = this.mockMvc.perform(
                                               put("/geods/import/geo_test")            
                                               .contentType (MediaType.APPLICATION_JSON)
                                               .content (dtoJson)
                                               .accept("application/json")
                                               .session (mockHttpSession)
                                               )            
     .andExpect (status ().isOk ())
     .andDo(print())
     .andReturn ();
    
  }

}
