package edu.dfci.cccb.mev.web.test.presets.controller.flat.small;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.simple.SimplePresetDescriptor;
import edu.dfci.cccb.mev.presets.util.timer.Timer;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;
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
                               ProbeAnnotationsPersistanceConfigTest.class                               
                               , TestJooqCursorLGGLevel2Configuration.class})
public class TestDatasetSerializerLGG {

  private @Inject Environment environment; 
  private @Inject @Named("presets-datasource") DataSource dataSource;
  private @Inject PresetDatasetBuilder presetDatasetBuilder;
    private @Inject Workspace workspace;  
  private URL rootUrl;  
  @PostConstruct
  public void init() throws MalformedURLException{
    this.rootUrl = new URL("file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/");
  }
  
  @Autowired WebApplicationContext applicationContext;  
  private MockMvc mockMvc;
  MockHttpSession mocksession;
  
  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    MockHttpServletRequest request = new MockHttpServletRequest();
    mocksession = new MockHttpSession();
    request.setSession(mocksession);
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    applicationContext.getBean(Workspace.class);
  }
  
  
  @Test @Ignore
  public void testSerializeDatasetJsonGeneratorSerializerProvider () throws Exception {
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";    
    PresetDescriptor descriptor = new SimplePresetDescriptor (tsvFileName, 
                                                              rootUrl, 
                                                              "LGG/Level_2/"+tsvFileName, ""); 
    Dataset presetDataset = presetDatasetBuilder.build (descriptor, "preset_test", null);
    log.debug("dataset.name: "+presetDataset.name ());
    
    
//    JsonFactory jfactory = new JsonFactory();
    String jsonFileName = tsvFileName+".flat.json";
    URL jsonURL = new URL(this.rootUrl, jsonFileName);
    log.debug("jsonURL:"+jsonURL);
      
    workspace.put (presetDataset);
    Timer timer = Timer.start ("GET-LGG-JSON-FLAT");
    this.mockMvc.perform(get("/dataset/preset_test/data").param ("format", "json")
                                            .session (mocksession)
                                            .accept("application/json"))            
            .andExpect (status ().isOk ())
            .andReturn ();
    timer.read ();
//            .andDo (new FilePrintingResultHandler (jsonURL));
//    String content = result.getResponse().getContentAsString();    
//    log.debug ("content:"+content);
  }

}
