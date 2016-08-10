package edu.dfci.cccb.mev.web.test.dataset.rest.controller.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationProjectManagerConfiguration;
import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration;
import edu.dfci.cccb.mev.dataset.domain.contract.*;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;
import edu.dfci.cccb.mev.test.annotation.server.configuration.ProbeAnnotationsPersistanceConfigTest;
import edu.dfci.cccb.mev.web.configuration.DispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.PersistenceConfiguration;
import edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations;
import lombok.extern.log4j.Log4j;
import org.junit.Before;
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

import javax.inject.Inject;
import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class TestAnalysisController {

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

  public static class AnalysisStatusX extends AbstractAnalysis<AnalysisStatusX> implements Analysis{}

  @Test
  public void testDeleteAnalysisOK () throws Exception {

    //create analysis
    mockDataset.analyses().put(new AnalysisStatusX().type ("dummy").name ("dummy1").status (Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));
    mockDataset.analyses().put(new AnalysisStatusX().type ("dummy").name ("dummy2").status (Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));
    mockDataset.analyses().put(new AnalysisStatusX().type ("dummy").name ("dummy3").status (Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));

    @SuppressWarnings("unused")
    MvcResult mvcResult = this.mockMvc.perform(delete("/dataset/mock_set/analysis/dummy1")
            .session (mockHttpSession)
            .accept("application/json"))
            .andExpect (status ().isOk ())
            .andDo(print())
            .andReturn ();

    assertThat(mockDataset.analyses().list(), equalTo(Arrays.asList(new String[]{"dummy3", "dummy2"})));
  }


}
