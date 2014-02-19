package edu.dfci.cccb.mev.test.presets.controller;

import org.junit.Test;
/*
@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes={DispatcherConfiguration.class, PersistenceConfiguration.class, ContainerConfigurations.class, DatasetRestConfiguration.class, 
                               LimmaRestConfiguration.class, HclRestConfiguration.class, AnnotationServerConfiguration.class
                               , PresetsRestConfiguration.class
                               })
*/
public class TestPresetsRestController {
  @Test
  public void test(){
    
  }
/*
  @Autowired WebApplicationContext applicationContext;  
  private MockMvc mockMvc;
  
  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
  }
  
  @Test
  public void testGetTcgaPresets () throws Exception {
    MvcResult result = this.mockMvc.perform(get("/presets/tcga").param ("format", "json").accept("application/json"))
            .andExpect (status ().isOk ())            
            .andExpect (MockMvcResultMatchers.jsonPath ("$[0].name").exists ())
            .andExpect (MockMvcResultMatchers.jsonPath ("$[0].disease").exists ())
            .andExpect (MockMvcResultMatchers.jsonPath ("$[0].diseaseName").exists ())
            .andExpect (MockMvcResultMatchers.jsonPath ("$[0].platform").exists ())
            .andExpect (MockMvcResultMatchers.jsonPath ("$[0].platformName").exists ())
            .andExpect (MockMvcResultMatchers.jsonPath ("$[0].dataLevel").exists ())
            .andExpect (MockMvcResultMatchers.jsonPath ("$[0].descriptor").doesNotExist ())
            .andReturn ();
    String content = result.getResponse().getContentAsString();    
  }
  
  @Test 
  public void testPresetListView () throws Exception {
    this.mockMvc.perform(get("/container/view/elements/presets/presetList"))
    .andExpect(status().isOk())
    .andReturn ();
    
    this.mockMvc.perform(get("/container/javascript/presets/PresetManager.js"))
    .andExpect(status().isOk())
    .andReturn ();

  }
  
*/
}
