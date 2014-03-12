package edu.dfci.cccb.mev.web.test.presets.controller.flat.small;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.ROW;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Scope;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.apache.log4j.Level;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.PrintingResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.rest.assembly.json.simple.SimpleDatasetJsonSerializer;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.hcl.rest.configuration.HclRestConfiguration;
import edu.dfci.cccb.mev.limma.rest.configuration.LimmaRestConfiguration;
import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.simple.SimplePresetDescriptor;
import edu.dfci.cccb.mev.presets.util.timer.Timer;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;
import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;
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
  private @Inject PresetValuesLoader loader;
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
    File jsonFile = new File(jsonURL.toURI ());
    FileOutputStream jsonFileStream = new FileOutputStream(jsonFile);
//    
//    try(OutputStreamWriter out = new OutputStreamWriter(jsonFileStream);){
//        JsonGenerator jgen = jfactory.createGenerator (new FileOutputStream (jsonFile));
//        
//        SimpleDatasetJsonSerializer jsonSerializer = new SimpleDatasetJsonSerializer();  
//        jsonSerializer.serialize (presetDataset, jgen, provider);
//      }    
//    
      
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

  private static class FilePrintingResultHandler extends PrintingResultHandler {
    
    
    public FilePrintingResultHandler(URL jsonURL) {      
      super(
            (new ResultValuePrinter() {
              OutputStreamWriter writer=null;
              
              @Override
              public void printHeading(String heading) {
      //          System.out.println();
      //          System.out.println(String.format("%20s:", heading));
              }
      
              @Override
              public void printValue(String label, Object value) {
                if (value != null && value.getClass().isArray()) {
                  value = CollectionUtils.arrayToList(value);
                }
                try {
                  writer.write (String.format("%20s = %s", label, value));
                  writer.flush ();
                } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              }
              
              public ResultValuePrinter init(URL jsonURL){
                try {
                  writer = new OutputStreamWriter (new FileOutputStream(new File(jsonURL.toURI ())));
                } catch (FileNotFoundException | URISyntaxException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
                return this;
              }
      }).init(jsonURL));
            
      
    }
  }
}
