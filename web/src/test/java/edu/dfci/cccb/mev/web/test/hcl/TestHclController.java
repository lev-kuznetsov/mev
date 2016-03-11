package edu.dfci.cccb.mev.web.test.hcl;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisNotFoundException;
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
import edu.dfci.cccb.mev.hcl.domain.contract.Node;
import edu.dfci.cccb.mev.hcl.domain.mock.MockBranch;
import edu.dfci.cccb.mev.hcl.domain.simple.SimpleHcl;
import edu.dfci.cccb.mev.hcl.rest.configuration.HclRestConfiguration;
import edu.dfci.cccb.mev.hcl.rest.controllers.HclAnalysisController;
import edu.dfci.cccb.mev.hcl.rest.controllers.HclAnalysisController.HclDto;
import edu.dfci.cccb.mev.pca.domain.PcaBuilder;
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
        HclRestConfiguration.class
        })
public class TestHclController {

	  private MockMvc mockMvc;
	  @Autowired WebApplicationContext applicationContext;
	  private MockHttpSession mockHttpSession;	 
	  @Inject DatasetBuilder datasetBuilder;  
	  private @Inject Workspace workspace;  
	  private @Inject ObjectMapper jsonObjectMapper;
	  Dataset dataset;  
	  @Inject @Named("pca.analysis.builder") Provider<PcaBuilder> builderProvider; 
	  
	   
	  private String jsonResult = "{\"name\":\"hcl_cols\",\"timestamp\":{\"timeInMillis\":1457387955748,\"seconds\":15,\"minutes\":59,\"hours\":4,\"period\":\"PM\"},\"type\":\"Hierarchical Clustering\",\"root\":{\"distance\":0.0,\"children\":[{\"distance\":3.1877,\"children\":[{\"distance\":2.594,\"children\":[{\"name\":\"C\"},{\"name\":\"A\"}]},{\"name\":\"B\"}]},{\"distance\":1.1129,\"children\":[{\"name\":\"F\"},{\"distance\":2.9154,\"children\":[{\"name\":\"D\"},{\"name\":\"E\"}]}]}]},\"dimension\":\"column\"}";
	  private String jsonResultRoot = "\"root\": {\n" + 
	  		"		\"distance\": 0.0,\n" + 
	  		"		\"children\": [{\n" + 
	  		"			\"distance\": 3.1877,\n" + 
	  		"			\"children\": [{\n" + 
	  		"				\"distance\": 2.594,\n" + 
	  		"				\"children\": [{\n" + 
	  		"					\"name\": \"C\"\n" + 
	  		"				}, {\n" + 
	  		"					\"name\": \"A\"\n" + 
	  		"				}]\n" + 
	  		"			}, {\n" + 
	  		"				\"name\": \"B\"\n" + 
	  		"			}]\n" + 
	  		"		}, {\n" + 
	  		"			\"distance\": 1.1129,\n" + 
	  		"			\"children\": [{\n" + 
	  		"				\"name\": \"F\"\n" + 
	  		"			}, {\n" + 
	  		"				\"distance\": 2.9154,\n" + 
	  		"				\"children\": [{\n" + 
	  		"					\"name\": \"D\"\n" + 
	  		"				}, {\n" + 
	  		"					\"name\": \"E\"\n" + 
	  		"				}]\n" + 
	  		"			}]\n" + 
	  		"		}]\n" + 
	  		"	}";
	 
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
	
	@Test @Ignore
	public void testCols() throws Exception {		
		HclDto dto = new HclDto("hcl_cols", "column", "euclidean", "complete", null, null);
	    run(dto);
	}
	@Test @Ignore
	public void testRows() throws Exception {		
		HclDto dto = new HclDto("hcl_rows", "row", "euclidean", "complete", null, null);
		run(dto);
	}

	private void run(HclDto dto) throws Exception, JsonProcessingException,
			AnalysisNotFoundException, InterruptedException, IOException,
			JsonParseException, JsonMappingException,
			UnsupportedEncodingException {
		@SuppressWarnings ("unused")
	    MvcResult mvcResult = this.mockMvc.perform(
	                                               post(String.format("/dataset/%s/analyze/hcl", dataset.name()))
	                                               .param ("format", "json")
	                                               .contentType (MediaType.APPLICATION_JSON)                                               
	                                               .accept("application/json")
	                                               .session (mockHttpSession)
	                                               .content(jsonObjectMapper.writeValueAsString(dto))
	                                               )            
	     .andExpect (status ().isOk ())
	     .andDo(print())
	     .andReturn ();

	      //The first put will generate an AnalysisStatus object with "IN_PROGRESS" status
	      Analysis analysisStatus = dataset.analyses ().get (dto.name());
	      log.debug("******* AnalysisStatus:\n"+ jsonObjectMapper.writeValueAsString (analysisStatus));      
	      assertThat(analysisStatus.name (), is(dto.name()));        
	      assertThat(analysisStatus.type (), is(SimpleHcl.ANALYSIS_TYPE));        
	      assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));
	      
	      //Wait for analysis to complete
	      Thread.sleep (5000L);
	    
	      //get the analysis directly from workspace
	      SimpleHcl analysis = (SimpleHcl) dataset.analyses ().get (dto.name());
	      log.debug("******* SimpleHclAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));      
	      assertThat(analysis.name (), is(dto.name()));      
	      assertThat (analysis.root(), not(nullValue ()));	      
	      SimpleHcl result = jsonObjectMapper.readValue(jsonResult, SimpleHcl.class);
//	      assertThat (analysis.dimension(), is(result.dimension()));
//	      MockBranch root = jsonObjectMapper.readValue(jsonResultRoot, MockBranch.class);
	      
	      
	      //get the analysis via rest
	      MvcResult mvcResultGET = this.mockMvc.perform (get(String.format("/dataset/%s/analysis/%s", dataset.name(), dto.name()))
	                                                     .contentType (MediaType.APPLICATION_JSON)                                               
	                                                     .accept("application/json")
	                                                     .session (mockHttpSession)
	                                                     .param ("format", "json")                                                     
	                                                     )
	     .andExpect (status ().isOk ())
	     .andDo(print())
	     .andReturn ();                                                
	      SimpleHcl analysisFromGET = jsonObjectMapper.readValue (mvcResultGET.getResponse ().getContentAsString (), SimpleHcl.class); 
	      assertThat (analysisFromGET.name (), is(dto.name()));
	      assertThat (analysisFromGET.name(), is(dto.name()));
	      
	      assertThat(analysisFromGET.type (), is(SimpleHcl.ANALYSIS_TYPE));
	}

}
