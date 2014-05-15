package edu.dfci.cccb.mev.web.test.presets.controller.flat.stress;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsConfigurationMain;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;
import edu.dfci.cccb.mev.presets.simple.SimplePresetDescriptor;
import edu.dfci.cccb.mev.presets.util.timer.Timer;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;
import edu.dfci.cccb.mev.test.presets.tools.PresetsRestToolConfiguration;
import edu.dfci.cccb.mev.web.configuration.DispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.PersistenceConfiguration;
import edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations;
import edu.dfci.cccb.mev.web.test.presets.controller.flat.large.TestJooqCursorGBMLevel2Configuration;

@Log4j
@WebAppConfiguration
@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DispatcherConfiguration.class, 
                               PersistenceConfiguration.class, 
                               ContainerConfigurations.class, 
                               DatasetRestConfiguration.class,
                               PresetRestPerfConfig.class,
                               ProbeAnnotationsConfigurationMain.class,
                               })
public class TestPresetHCL {

  private @Inject Environment environment; 
  private @Inject @Named("presets-datasource") DataSource dataSource;
  private @Inject PresetDatasetBuilder presetDatasetBuilder;
//  private @Inject PresetValuesLoader loader;
  private @Inject Workspace workspace;  
  private @Inject PresetDimensionBuilder dimensionBuilder;
  private @Inject Presets presets;
  
  private URL dataRootUrl;
  private URL annotationsRootUrl ;  
  @PostConstruct
  public void init() throws MalformedURLException, InterruptedException{
    this.dataRootUrl = new URL("file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/");    
    this.annotationsRootUrl = new URL("file://"+environment.getProperty ("user.home")+"/mev/data/array_annotations/");
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();    
  }
  
  @Autowired WebApplicationContext applicationContext;  
  private MockMvc mockMvc;
  MockHttpSession mocksession;
  
  @Rule
  public ContiPerfRule i = new ContiPerfRule ();
  private static int testCount=0;
  
  @Before
  public void setup() throws InterruptedException {

  }
  
  @Test  
  @PerfTest(invocations=1, threads=1)
  public void test() throws Exception{    
//    internalRun (mockMvc, "LUSC.HT_HG-U133A.Level_3.tsv", "LUSC/Level_3");    
    Random rand = new Random();
    Preset preset = presets.getAll ().get (rand.nextInt(presets.getAll ().size ()));
    log.debug ("*****TEST WITH "+preset.name ());    
    internalRun (mockMvc, preset);
  }
  
//  private void internalRun (MockMvc mockMvc, String tsvFileName, String folder) throws Exception {
  private void internalRun (MockMvc mockMvc, Preset preset) throws Exception {
//    String tsvFileName="GBM.AgilentG4502A_07_2.Level_2.tsv";  
//    String folder="GBM/Level_2/";    
    MockHttpSession mocksession = new MockHttpSession();
    MockHttpServletRequest request = new MockHttpServletRequest();    
    request.setSession(mocksession);
    
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    applicationContext.getBean(Workspace.class);
    
//    PresetDescriptor descriptor = new SimplePresetDescriptor ("PRESET-"+tsvFileName, 
//                                                              dataRootUrl, 
//                                                              folder+tsvFileName, "",
//                                                              annotationsRootUrl, "");
    
    PresetDescriptor descriptor = preset.descriptor ();
    Dimension columns = dimensionBuilder.buildColumns (descriptor);
    List<String> columnList1=new ArrayList<String> (50);
    Random rand = new Random ();
    for(int i=0; i<50; i++){
      int randomCol = rand.nextInt (columns.keys ().size ());
      columnList1.add (columns.keys ().get (randomCol));
    }
    Selection selection = new SimpleSelection("presest_test_selection", new Properties (), columnList1);
    Dataset presetDataset = presetDatasetBuilder.build (descriptor, "preset_test", selection, null);
    log.debug("dataset.name: "+presetDataset.name ());
        
    workspace.put (presetDataset);
    Timer timer = Timer.start ("SELECTION:POST");
    this.mockMvc.perform(post("/dataset/preset_test/column/selection").param ("format", "json")
                                            .session (mocksession)
                                            .accept("application/json")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content ("{\"name\":\"sss\",\"properties\":{\"selectionDescription\":\"\",\"selectionColor\":\"#bbceb3\"},"+
                                            "\"keys\":[\""+presetDataset.dimension (Type.COLUMN).keys ().get (0)+"\"]}")
                                            )            
            .andExpect (status ().isOk ())
            .andReturn ();
    log.debug(presetDataset.dimension (Type.COLUMN).selections ().getAll ());
    timer.read ("selection posted");
    
    timer = Timer.start ("HCL:POST");
    this.mockMvc.perform(post("/dataset/preset_test/analyze/hcl").param ("format", "json")
                                            .session (mocksession)
                                            .accept("application/json")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content ("{\"name\":\"test_hcl\",\"dimension\":\"column\",\"metric\":\"euclidean\",\"linkage\":\"complete\"}")
                                            )            
            .andExpect (status ().isOk ())
            .andReturn ();
    log.debug(presetDataset.dimension (Type.COLUMN).selections ().getAll ());
    timer.read ("selection posted");
  }

  
  
}
