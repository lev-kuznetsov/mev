package edu.dfci.cccb.mev.web.test.survival.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Properties;

import javax.inject.Inject;

import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationProjectManagerConfiguration;
import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration;
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
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalAnalysis;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalInputEntryTcga;
import edu.dfci.cccb.mev.survival.domain.contract.SurvivalParams;
import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalAnalysis;
import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalInputEntryTcga;
import edu.dfci.cccb.mev.survival.domain.impl.SimpleSurvivalParams;
import edu.dfci.cccb.mev.survival.rest.configuration.SurvivalAnalysisConfiguration;
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
                               AnnotationProjectManagerConfiguration.class,
                               SurvivalAnalysisConfiguration.class
                               })
public class TestSurvivalController {
  
  @Autowired WebApplicationContext applicationContext;
  private MockMvc mockMvc;
  private MockHttpSession mockHttpSession;
  @Inject DatasetBuilder datasetBuilder;  
  private @Inject Workspace workspace;  
  private @Inject ObjectMapper jsonObjectMapper;
  
//  @PostConstruct
//  public void init() throws MalformedURLException{
//    this.dataRootUrl = new URL("file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/");    
//    this.annotationsRootUrl = new URL("file://"+environment.getProperty ("user.home")+"/mev/data/array_annotations/");
//  }
  
  @Before
  public void setup(){
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    MockHttpServletRequest request = new MockHttpServletRequest();
    mockHttpSession = new MockHttpSession();
    request.setSession(mockHttpSession);
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    applicationContext.getBean(Workspace.class);
  }
  
  @SuppressWarnings ("serial")
  @Test @Ignore
  public void test () throws Exception {
//    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";    
//    String folder="LGG/Level_2/";
//    PresetDescriptor descriptor = new SimplePresetDescriptor (tsvFileName, 
//                                                              dataRootUrl, 
//                                                              folder+tsvFileName, "",
//                                                              annotationsRootUrl, "");
//    Dataset presetDataset = presetDatasetBuilder.build (descriptor, "survival_test", null, null);
//    log.debug("dataset.name: "+presetDataset.name ());
    
    
//    JsonFactory jfactory = new JsonFactory();
//    String jsonFileName = tsvFileName+".json";
//    URL jsonURL = new URL(this.dataRootUrl, jsonFileName);
//    log.debug("jsonURL:"+jsonURL);
    
    //create dataset
    Dataset dataset = datasetBuilder.build (new MockTsvInput ("survival_test", "\tS1\tS2\tS3\tS4\tS5\tS6\tS7\tS8\tS9\tS10\nG1\t1.188514712463404699e+01\t1.204140233660780979e+01\t1.234081412888200902e+01\t1.163898490634033323e+01\t1.236031434743662594e+01\t1.263358678802757851e+01\t8.279956752711142842e-01\t1.370350032209602054e+00\t1.037994449169919786e+00\t1.799723880674055643e-01\nG2\t1.885238297671112173e+00\t1.336588864883923256e+00\t8.726933138377110932e-01\t1.000704965530543999e+00\t1.217150222216657252e+00\t1.665733953332364292e+00\t9.910461446315059764e-01\t8.893733573944051507e-01\t7.208001799521422948e-01\t1.426827801481250546e+00\nG3\t1.188388854775392689e+00\t2.248097448451613634e-02\t4.585539956464407263e-01\t6.321050648189079357e-01\t2.288129746374324203e+00\t1.450523320935948490e+00\t8.743715686250737307e-02\t1.158676271225091536e+00\t4.483649242829604864e-01\t9.220164357611759787e-01\nG4\t1.262581518849290063e+01\t1.097643408373854257e+01\t1.248733733542591118e+01\t1.183359244927881093e+01\t1.242754369105501233e+01\t1.154062810662884253e+01\t1.402161753382923148e+00\t1.110723816716849388e+00\t5.519559139121001934e-01\t1.191073330691207799e+00\nG5\t4.212630682277810901e-01\t1.237089222272245159e+00\t3.942134331403249758e-01\t6.571736269682573184e-01\t7.107561591312392402e-01\t-1.799850179649653548e-02\t5.900194955064834712e-01\t1.866021689210231704e+00\t1.727950989673915139e+00\t1.673238760870431552e+00\nG6\t1.623904902317880872e+00\t2.033737954042716289e+00\t1.059667307687251370e+00\t5.829633567125455107e-01\t7.066180780581627907e-01\t7.179278333133176648e-01\t-1.620837539166711527e-01\t1.103900303479948608e+00\t1.000963636766493581e+00\t1.224700740396261756e+00\nG7\t1.089347908225312800e+00\t5.027904142354412409e-01\t1.526308400742132365e+00\t7.910323975010494779e-01\t5.334721895871848973e-01\t-6.584953162406614879e-02\t1.487086356403074960e+00\t7.289110071229999432e-01\t9.902665697489260177e-01\t5.816465061333875308e-01\nG8\t8.485257423886340966e-01\t2.465461094159465461e+00\t1.349261064626203988e+00\t6.143236099481832468e-01\t1.185825425854624138e+00\t1.244199580018195528e+00\t1.853690475642675750e+00\t-3.717669448902238827e-02\t6.854264339399868122e-01\t1.383830132326594464e-01\nG9\t1.403874515733180672e+00\t2.140084051686685029e+00\t1.143839053529577043e+00\t1.207999104285488734e+00\t1.209300244849931039e+00\t2.379590313511170674e-02\t1.974749101374898563e+00\t1.070609451318704108e+00\t6.151091514422060008e-02\t9.736475782769393739e-01\nG10\t7.899488648703185678e-01\t1.999391228179024704e+00\t9.363406180457896077e-01\t8.661591803373676068e-01\t9.989733067095830643e-01\t6.946825858089700212e-01\t9.689452303609590311e-01\t8.093542982306978217e-02\t1.095035589870868353e+00\t9.723302437465745074e-01\nG11\t3.334731085908727577e-01\t3.426242846092957262e-01\t4.937020871446952397e-02\t4.302553294755867563e-01\t1.153920107411710028e+00\t1.070122611874620544e+00\t1.908926108120575504e+00\t1.952270474332367778e+00\t7.019370450079993884e-02\t4.295688624516629250e-01\nG12\t1.392406509736836329e+00\t1.372870779384700590e+00\t5.404078368283642408e-01\t9.577725746500534987e-01\t1.930301589849148236e+00\t1.065669702544272957e+00\t1.022885904312618166e+00\t6.427143424737080668e-01\t3.930179936499352267e-01\t9.990896938741014033e-01\nG13\t1.325633714376544425e+00\t7.040364121034963230e-01\t1.589435388962163076e+00\t2.284719993548981698e+00\t1.702734362224248121e+00\t1.412491307931115214e+00\t1.672459629461545694e+00\t1.073155180531838671e+00\t1.411681767952686073e+00\t6.238961028115959095e-01\nG14\t1.288950803566242254e+00\t1.028575691034351314e-01\t1.102664086811557587e+00\t4.699036879893047081e-01\t3.876866425390312898e-01\t1.416878751168134887e+00\t9.433454890035535279e-01\t1.413906277410074352e+00\t4.778216530655552963e-01\t4.268871754146941200e-02\nG15\t1.322327157267192854e+00\t1.030668314264236773e+00\t1.171973213151947357e+00\t7.051026378379774417e-01\t1.603389007795869015e-01\t1.013608197193421656e+00\t1.119755089175789919e+00\t8.846284618247358678e-01\t1.032390130479277301e+00\t1.563217432008172647e-01\nG16\t4.594649795375105317e-02\t1.391620831679649006e+00\t1.543638392839115081e+00\t1.250790629220879691e+00\t1.760626870246531972e+00\t-2.719273644194430961e-01\t1.228157198567842556e+00\t5.718455067192516061e-01\t1.223007328253810089e+00\t1.320469664101578511e+00\nG17\t1.277799857436148834e+01\t1.275249893990934069e+01\t1.233011570757031095e+01\t1.245797072549325613e+01\t1.144397636751770087e+01\t1.264147576903921966e+01\t9.861790550751070583e-01\t1.005304796569497894e+00\t9.890599521756782142e-01\t8.110532346285517225e-01\nG18\t1.581739895645190686e+00\t2.054658403772655628e-01\t1.277027695188764245e+00\t1.460942482509764773e+00\t1.475607347809501535e+00\t4.862547500778641174e-01\t3.462212302333083080e-01\t1.181704073194318472e+00\t1.447215677951656065e+00\t1.239332864373165588e+00\nG19\t8.294625023542446796e-01\t4.347079214591751395e-01\t1.285626053170358718e+00\t1.773088394837966764e+00\t1.184014487786790193e+00\t5.522823407713599586e-01\t5.705450257984490126e-01\t2.110152326668979228e+00\t1.325059483533218563e+00\t2.663088059378119210e-01\nG20\t7.120160033627074281e-01\t9.123263397371228489e-01\t1.431553131076708674e+00\t1.724438313837745040e+00\t1.327524752620593329e+00\t2.585233328583039780e-01\t9.156477637307659512e-01\t8.222344601683477805e-01\t7.215323977152153789e-01\t6.489097589989401005e-01"));    
    dataset.dimension (Type.COLUMN).selections ().put (new SimpleSelection ("eee", new Properties (), new ArrayList<String>(){
      {
        add("TCGA-A7-A0D9-11A-53R-A089-07");
        add("TCGA-A7-A13F-01A-11R-A12P-07");
        add("TCGA-A7-A0CG-01A-12R-A056-07");
        add("TCGA-A7-A0CJ-01A-21R-A00Z-07");
      }
    }));
    dataset.dimension (Type.COLUMN).selections ().put (new SimpleSelection ("ccc", new Properties (), new ArrayList<String>(){
      {
        add("TCGA-A7-A0DC-01A-11R-A00Z-07");
        add("TCGA-A7-A0CE-01A-11R-A00Z-07");
        add("TCGA-A7-A0CD-01A-11R-A00Z-07");
        add("TCGA-A7-A13E-11A-61R-A12P-07");
      }
    }));
    workspace.put (dataset);
    
//    this.mockMvc.perform(get("/dataset/preset_test/data").param ("format", "json")
//                                            .session (mocksession)
//                                            .accept("application/json"))            
//            .andExpect (status ().isOk ())
//            .andReturn ();
        
    SurvivalParams survivalParams = new SimpleSurvivalParams ("survival1", dataset.name (), 
                                                              "eee", dataset.dimension (Type.COLUMN).selections ().get ("eee"), 
                                                              "ccc", dataset.dimension (Type.COLUMN).selections ().get ("ccc"),
      new ArrayList<SurvivalInputEntryTcga> (){
      {
        add(new SimpleSurvivalInputEntryTcga ("id1", 1000L, 0, 1, 1000L, null, "alive"));
        add(new SimpleSurvivalInputEntryTcga ("id2", 1000L, 0, 0, 1000L, null, "alive"));
        add(new SimpleSurvivalInputEntryTcga ("id3", 1000L, 0, 0, 1000L, null, "alive"));
      }
    }); 
    String dtoJson = jsonObjectMapper.writeValueAsString (survivalParams);
    log.debug("*************  survivalDtoJson:"+dtoJson);    
    SimpleSurvivalInputEntryTcga entry = jsonObjectMapper.readValue ("{  \n" + 
            "         \"key\":\"id1\",\n" + 
            "         \"days_to_death\":1000,\n" + 
            "         \"days_to_last_followup\":null,\n" + 
            "         \"vital_status\":\"alive\",\n" + 
            "         \"group\":0\n" + 
            "      }", SimpleSurvivalInputEntryTcga.class);    
    log.debug("************* survival entry:"+entry);
    SimpleSurvivalParams testParams = jsonObjectMapper.readValue (dtoJson, SimpleSurvivalParams.class);
    log.debug("************* survival params:"+testParams);
    SimpleSelection selection = jsonObjectMapper.readValue("{  \n" + 
            "      \"name\":\"eee\",\n" + 
            "      \"properties\":{  \n" + 
            "         \"selectionColor\":\"#bdee0b\",\n" + 
            "         \"selectionDescription\":\"\"\n" + 
            "      },\n" + 
            "      \"keys\":[  \n" + 
            "         \"TCGA-A7-A0CE-01A-11R-A00Z-07\",\n" + 
            "         \"TCGA-A7-A0DC-01A-11R-A00Z-07\",\n" + 
            "         \"TCGA-A7-A13F-01A-11R-A12P-07\"\n" + 
            "      ],\n" + 
            "      \"type\":\"column\"\n" + 
            "   }", SimpleSelection.class);
    log.debug("*********** simple selection"+selection);
    
    
    
//    dtoJson = "{\"name\":\"surv1\",\"datasetName\":\"brca\",\"experiment\":{\"name\":\"exp\",\"properties\":{\"selectionColor\":\"#18d239\",\"selectionDescription\":\"\"},\"keys\":[\"TCGA-A8-A09I-01A-22R-A034-07\",\"TCGA-A2-A0SW-01A-11R-A084-07\",\"TCGA-BH-A18R-11A-42R-A12D-07\",\"TCGA-AO-A03R-01A-21R-A034-07\",\"TCGA-AO-A0JL-01A-11R-A056-07\",\"TCGA-E2-A14O-01A-31R-A115-07\",\"TCGA-BH-A1ET-01A-11R-A137-07\",\"TCGA-B6-A0X7-01A-11R-A10J-07\"],\"type\":\"column\"},\"experimentName\":\"exp\",\"control\":{\"name\":\"ctr\",\"properties\":{\"selectionColor\":\"#9c22eb\",\"selectionDescription\":\"\"},\"keys\":[\"TCGA-AR-A1AT-01A-11R-A12P-07\",\"TCGA-BH-A18R-01A-11R-A12D-07\",\"TCGA-BH-A1ET-11B-23R-A137-07\",\"TCGA-E2-A109-01A-11R-A10J-07\",\"TCGA-AO-A0JI-01A-21R-A056-07\"],\"type\":\"column\"},\"controlName\":\"ctr\",\"input\":[{\"key\":\"TCGA-AO-A03R-01A-21R-A034-07\",\"days_to_death\":null,\"days_to_last_followup\":1707,\"vital_status\":\"Alive\",\"group\":2,\"status\":0,\"time\":1707},{\"key\":\"TCGA-AO-A0JI-01A-21R-A056-07\",\"days_to_death\":null,\"days_to_last_followup\":1172,\"vital_status\":\"Alive\",\"group\":1,\"status\":0,\"time\":1172},{\"key\":\"TCGA-E2-A109-01A-11R-A10J-07\",\"days_to_death\":null,\"days_to_last_followup\":1172,\"vital_status\":\"Alive\",\"group\":1,\"status\":0,\"time\":1172},{\"key\":\"TCGA-BH-A1ET-01A-11R-A137-07\",\"days_to_death\":2520,\"days_to_last_followup\":null,\"vital_status\":\"Dead\",\"group\":2,\"status\":1,\"time\":2520},{\"key\":\"TCGA-A2-A0SW-01A-11R-A084-07\",\"days_to_death\":1364,\"days_to_last_followup\":null,\"vital_status\":\"Dead\",\"group\":2,\"status\":1,\"time\":1364},{\"key\":\"TCGA-AO-A0JL-01A-11R-A056-07\",\"days_to_death\":null,\"days_to_last_followup\":1319,\"vital_status\":\"Alive\",\"group\":2,\"status\":0,\"time\":1319},{\"key\":\"TCGA-BH-A18R-11A-42R-A12D-07\",\"days_to_death\":1141,\"days_to_last_followup\":null,\"vital_status\":\"Dead\",\"group\":2,\"status\":1,\"time\":1141},{\"key\":\"TCGA-BH-A1ET-11B-23R-A137-07\",\"days_to_death\":2520,\"days_to_last_followup\":null,\"vital_status\":\"Dead\",\"group\":1,\"status\":1,\"time\":2520},{\"key\":\"TCGA-A8-A09I-01A-22R-A034-07\",\"days_to_death\":null,\"days_to_last_followup\":1006,\"vital_status\":\"Alive\",\"group\":2,\"status\":0,\"time\":1006},{\"key\":\"TCGA-E2-A14O-01A-31R-A115-07\",\"days_to_death\":null,\"days_to_last_followup\":1172,\"vital_status\":\"Alive\",\"group\":2,\"status\":0,\"time\":1172},{\"key\":\"TCGA-AR-A1AT-01A-11R-A12P-07\",\"days_to_death\":1272,\"days_to_last_followup\":null,\"vital_status\":\"Dead\",\"group\":1,\"status\":1,\"time\":1272},{\"key\":\"TCGA-BH-A18R-01A-11R-A12D-07\",\"days_to_death\":1141,\"days_to_last_followup\":null,\"vital_status\":\"Dead\",\"group\":1,\"status\":1,\"time\":1141},{\"key\":\"TCGA-B6-A0X7-01A-11R-A10J-07\",\"days_to_death\":1791,\"days_to_last_followup\":null,\"vital_status\":\"Dead\",\"group\":2,\"status\":1,\"time\":1791}]}";
    //this one results in upper_ci=Inf
    dtoJson = "{\"name\":\"survival\",\"datasetName\":\"blood\",\"experimentName\":\"lymphnodesYes\",\"experiment\":{\"name\":\"lymphnodesYes\",\"properties\":{\"selectionColor\":\"#07801e\",\"selectionFacetLink\":\"project?project=1576060861743&ui=%7B%22facets%22%3A%5B%7B%22c%22%3A%7B%22type%22%3A%22list%22%2C%22name%22%3A%22ethnicity%22%2C%22columnName%22%3A%22ethnicity%22%2C%22expression%22%3A%22value%22%2C%22omitBlank%22%3Afalse%2C%22omitError%22%3Afalse%2C%22selectBlank%22%3Afalse%2C%22selectError%22%3Afalse%2C%22invert%22%3Afalse%7D%2C%22o%22%3A%7B%22sort%22%3A%22name%22%7D%2C%22s%22%3A%5B%5D%7D%2C%7B%22c%22%3A%7B%22type%22%3A%22list%22%2C%22name%22%3A%22icd_10%22%2C%22columnName%22%3A%22icd_10%22%2C%22expression%22%3A%22value%22%2C%22omitBlank%22%3Afalse%2C%22omitError%22%3Afalse%2C%22selectBlank%22%3Afalse%2C%22selectError%22%3Afalse%2C%22invert%22%3Afalse%7D%2C%22o%22%3A%7B%22sort%22%3A%22name%22%7D%2C%22s%22%3A%5B%5D%7D%2C%7B%22c%22%3A%7B%22type%22%3A%22list%22%2C%22name%22%3A%22number_of_lymphnodes_positive_by_he%22%2C%22columnName%22%3A%22number_of_lymphnodes_positive_by_he%22%2C%22expression%22%3A%22value%22%2C%22omitBlank%22%3Afalse%2C%22omitError%22%3Afalse%2C%22selectBlank%22%3Afalse%2C%22selectError%22%3Afalse%2C%22invert%22%3Afalse%7D%2C%22o%22%3A%7B%22sort%22%3A%22name%22%7D%2C%22s%22%3A%5B%7B%22v%22%3A%7B%22v%22%3A%222%22%2C%22l%22%3A%222%22%7D%7D%2C%7B%22v%22%3A%7B%22v%22%3A%2210%22%2C%22l%22%3A%2210%22%7D%7D%2C%7B%22v%22%3A%7B%22v%22%3A%221%22%2C%22l%22%3A%221%22%7D%7D%2C%7B%22v%22%3A%7B%22v%22%3A%224%22%2C%22l%22%3A%224%22%7D%7D%5D%7D%5D%7D\",\"selectionDescription\":\"\"},\"keys\":[\"TCGA-A2-A0SY-01A-31R-A084-07\",\"TCGA-AO-A0JB-01A-11R-A056-07\",\"TCGA-A2-A0SW-01A-11R-A084-07\",\"TCGA-BH-A18R-11A-42R-A12D-07\",\"TCGA-AN-A0FW-01A-11R-A034-07\",\"TCGA-BH-A18N-01A-11R-A12D-07\",\"TCGA-AR-A1AT-01A-11R-A12P-07\",\"TCGA-BH-A18N-11A-43R-A12D-07\",\"TCGA-BH-A18R-01A-11R-A12D-07\"]},\"controlName\":\"lymphnodes0\",\"control\":{\"name\":\"lymphnodes0\",\"properties\":{\"selectionColor\":\"#cdc4b7\",\"selectionFacetLink\":\"project?project=1576060861743&ui=%7B%22facets%22%3A%5B%7B%22c%22%3A%7B%22type%22%3A%22list%22%2C%22name%22%3A%22ethnicity%22%2C%22columnName%22%3A%22ethnicity%22%2C%22expression%22%3A%22value%22%2C%22omitBlank%22%3Afalse%2C%22omitError%22%3Afalse%2C%22selectBlank%22%3Afalse%2C%22selectError%22%3Afalse%2C%22invert%22%3Afalse%7D%2C%22o%22%3A%7B%22sort%22%3A%22name%22%7D%2C%22s%22%3A%5B%5D%7D%2C%7B%22c%22%3A%7B%22type%22%3A%22list%22%2C%22name%22%3A%22icd_10%22%2C%22columnName%22%3A%22icd_10%22%2C%22expression%22%3A%22value%22%2C%22omitBlank%22%3Afalse%2C%22omitError%22%3Afalse%2C%22selectBlank%22%3Afalse%2C%22selectError%22%3Afalse%2C%22invert%22%3Afalse%7D%2C%22o%22%3A%7B%22sort%22%3A%22name%22%7D%2C%22s%22%3A%5B%5D%7D%2C%7B%22c%22%3A%7B%22type%22%3A%22list%22%2C%22name%22%3A%22number_of_lymphnodes_positive_by_he%22%2C%22columnName%22%3A%22number_of_lymphnodes_positive_by_he%22%2C%22expression%22%3A%22value%22%2C%22omitBlank%22%3Afalse%2C%22omitError%22%3Afalse%2C%22selectBlank%22%3Afalse%2C%22selectError%22%3Afalse%2C%22invert%22%3Afalse%7D%2C%22o%22%3A%7B%22sort%22%3A%22name%22%7D%2C%22s%22%3A%5B%7B%22v%22%3A%7B%22v%22%3A%220%22%2C%22l%22%3A%220%22%7D%7D%5D%7D%5D%7D\",\"selectionDescription\":\"\"},\"keys\":[\"TCGA-BH-A0BO-01A-23R-A12D-07\",\"TCGA-AR-A1AW-01A-21R-A12P-07\",\"TCGA-BH-A0H3-01A-11R-A12P-07\",\"TCGA-BH-A18V-11A-52R-A12D-07\",\"TCGA-BH-A18V-01A-11R-A12D-07\",\"TCGA-BH-A0H5-11A-62R-A115-07\",\"TCGA-BH-A0H5-01A-21R-A115-07\"]},\"input\":[{\"key\":\"TCGA-A2-A0SY-01A-31R-A084-07\",\"time\":1154,\"status\":0,\"group\":2,\"days_to_death\":null,\"days_to_last_followup\":1154,\"vital_status\":\"Alive\"},{\"key\":\"TCGA-BH-A0BO-01A-23R-A12D-07\",\"time\":1085,\"status\":0,\"group\":1,\"days_to_death\":null,\"days_to_last_followup\":1085,\"vital_status\":\"Alive\"},{\"key\":\"TCGA-AO-A0JB-01A-11R-A056-07\",\"time\":1150,\"status\":0,\"group\":2,\"days_to_death\":null,\"days_to_last_followup\":1150,\"vital_status\":\"Alive\"},{\"key\":\"TCGA-A2-A0SW-01A-11R-A084-07\",\"time\":1364,\"status\":1,\"group\":2,\"days_to_death\":1364,\"days_to_last_followup\":null,\"vital_status\":\"Dead\"},{\"key\":\"TCGA-AR-A1AW-01A-21R-A12P-07\",\"time\":1072,\"status\":0,\"group\":1,\"days_to_death\":null,\"days_to_last_followup\":1072,\"vital_status\":\"Alive\"},{\"key\":\"TCGA-BH-A18R-11A-42R-A12D-07\",\"time\":1141,\"status\":1,\"group\":2,\"days_to_death\":1141,\"days_to_last_followup\":null,\"vital_status\":\"Dead\"},{\"key\":\"TCGA-BH-A0H3-01A-11R-A12P-07\",\"time\":1149,\"status\":0,\"group\":1,\"days_to_death\":null,\"days_to_last_followup\":1149,\"vital_status\":\"Alive\"},{\"key\":\"TCGA-BH-A18V-11A-52R-A12D-07\",\"time\":1555,\"status\":1,\"group\":1,\"days_to_death\":1555,\"days_to_last_followup\":null,\"vital_status\":\"Dead\"},{\"key\":\"TCGA-AN-A0FW-01A-11R-A034-07\",\"time\":11,\"status\":0,\"group\":2,\"days_to_death\":null,\"days_to_last_followup\":11,\"vital_status\":\"Alive\"},{\"key\":\"TCGA-BH-A18V-01A-11R-A12D-07\",\"time\":1555,\"status\":1,\"group\":1,\"days_to_death\":1555,\"days_to_last_followup\":null,\"vital_status\":\"Dead\"},{\"key\":\"TCGA-BH-A18N-01A-11R-A12D-07\",\"time\":1148,\"status\":1,\"group\":2,\"days_to_death\":1148,\"days_to_last_followup\":null,\"vital_status\":\"Dead\"},{\"key\":\"TCGA-AR-A1AT-01A-11R-A12P-07\",\"time\":1272,\"status\":1,\"group\":2,\"days_to_death\":1272,\"days_to_last_followup\":null,\"vital_status\":\"Dead\"},{\"key\":\"TCGA-BH-A0H5-11A-62R-A115-07\",\"time\":1080,\"status\":0,\"group\":1,\"days_to_death\":null,\"days_to_last_followup\":1080,\"vital_status\":\"Alive\"},{\"key\":\"TCGA-BH-A18N-11A-43R-A12D-07\",\"time\":1148,\"status\":1,\"group\":2,\"days_to_death\":1148,\"days_to_last_followup\":null,\"vital_status\":\"Dead\"},{\"key\":\"TCGA-BH-A18R-01A-11R-A12D-07\",\"time\":1141,\"status\":1,\"group\":2,\"days_to_death\":1141,\"days_to_last_followup\":null,\"vital_status\":\"Dead\"},{\"key\":\"TCGA-BH-A0H5-01A-21R-A115-07\",\"time\":1080,\"status\":0,\"group\":1,\"days_to_death\":null,\"days_to_last_followup\":1080,\"vital_status\":\"Alive\"}]}";
    SimpleSurvivalParams params = jsonObjectMapper.readValue (dtoJson, SimpleSurvivalParams.class);
    @SuppressWarnings ("unused")
    MvcResult mvcResult = this.mockMvc.perform(
                                               post("/dataset/survival_test/analyze/"+params.name ())
                                               .param ("format", "json")
                                               .contentType (MediaType.APPLICATION_JSON)
                                               .content (dtoJson)
                                               .accept("application/json")
                                               .session (mockHttpSession)
                                               )            
     .andDo(print())
     .andExpect (status ().isOk ())     
     .andReturn ();
    
    //The first put will generate an AnalysisStatus object with "IN_PROGRESS" status    
    Analysis analysisStatus = dataset.analyses ().get (params.name ());
    log.debug("******* AnalysisStatus:\n"+ jsonObjectMapper.writeValueAsString (analysisStatus));      
    assertThat(analysisStatus.name (), is(params.name ()));        
    assertThat(analysisStatus.type (), is(SurvivalAnalysis.ANALYSIS_TYPE));        
    assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));        
     
    //Wait for analysis to complete
    Thread.sleep (2000L);
        
    SimpleSurvivalAnalysis analysis = (SimpleSurvivalAnalysis) dataset.analyses ().get (params.name ());
    assertThat(analysis.name (), is(params.name()));      
    assertThat(analysis.type (), is(SurvivalAnalysis.ANALYSIS_TYPE));        
    assertThat(analysis.status (), is(Analysis.MEV_ANALYSIS_STATUS_SUCCESS));
    log.debug("******* SimpleSurvivalAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));
      
  }

}
