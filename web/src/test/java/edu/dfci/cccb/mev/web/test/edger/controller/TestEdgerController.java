package edu.dfci.cccb.mev.web.test.edger.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dfci.cccb.mev.dataset.domain.contract.*;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;
import edu.dfci.cccb.mev.edger.domain.Edge;
import edu.dfci.cccb.mev.edger.rest.EdgeConfiguration;
import edu.dfci.cccb.mev.web.configuration.DispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.PersistenceConfiguration;
import edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations;
import lombok.extern.log4j.Log4j;
import org.junit.Before;
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

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by antony on 4/19/16.
 */
@Log4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DispatcherConfiguration.class,
        PersistenceConfiguration.class,
        ContainerConfigurations.class,
        DatasetRestConfiguration.class,
        RDispatcherConfiguration.class,
        EdgeConfiguration.class
})
public class TestEdgerController {

    @Autowired private WebApplicationContext applicationContext;
    private MockMvc mockMvc;
    private MockHttpSession mockHttpSession;
    @Inject DatasetBuilder datasetBuilder;
    private @Inject Workspace workspace;
    private @Inject ObjectMapper jsonObjectMapper;
    Dataset dataset;
    private @Inject Provider<SelectionBuilder> selectionBuilder;

    @Before
    public void setup() throws IOException, InvalidDimensionTypeException, InvalidDatasetNameException, DatasetBuilderException{
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        mockHttpSession = new MockHttpSession();
        request.setSession(mockHttpSession);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        applicationContext.getBean(Workspace.class);

        URL urlData = this.getClass ().getResource ("/mouse_test_data.tsv");
        RawInput input = new UrlTsvInput(urlData);
        dataset = new SimpleDatasetBuilder().setParserFactories (asList (new SuperCsvParserFactory()))
                .setValueStoreBuilder (new FlatFileValueStoreBuilder())
                .build (input);
        workspace.put (dataset);
    }

    @Test
    public void testAsync() throws Exception {
        Selection control = jsonObjectMapper.readValue("{\"name\":\"s1\",\"properties\":{\"selectionColor\":\"#5fd97b\",\"selectionDescription\":\"\"},\"keys\":[\"A\",\"B\",\"C\"]}", SimpleSelection.class);
        Selection experiment = jsonObjectMapper.readValue("{\"name\":\"s2\",\"properties\":{\"selectionColor\":\"#b0220e\",\"selectionDescription\":\"\"},\"keys\":[\"D\",\"E\",\"F\"]}", SimpleSelection.class);
        Edge.EdgeParams dto = new Edge.EdgeParams("edger-test", experiment, control, "fdr");
        MvcResult mvcResult = this.mockMvc.perform(
                put(String.format("/dataset/%s/analyze/edger/%s", dataset.name(), dto.name()))
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
        assertThat(analysisStatus.type (), is("edger"));
        assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));

        //Wait for analysis to complete
        Thread.sleep (10000L);

        //get the analysis directly from workspace
        log.debug("**************************");
        Edge analysis =  (Edge)dataset.analyses ().get (dto.name());
        log.debug("******* EdgerAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));
        assertThat(analysis.name (), is(dto.name()));
        assertThat(analysis.status (), is(Analysis.MEV_ANALYSIS_STATUS_SUCCESS));

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
        Edge analysisFromGET = jsonObjectMapper.readValue (mvcResultGET.getResponse ().getContentAsString (), Edge.class);
        assertThat (analysisFromGET.name (), is(dto.name()));
        assertNotNull (analysisFromGET.params());
        assertNotNull (analysisFromGET.results ());
        assertThat(analysisFromGET.results ().size(), greaterThan(0));
        assertThat(analysisFromGET.status (), is(Analysis.MEV_ANALYSIS_STATUS_SUCCESS));
    }

}
