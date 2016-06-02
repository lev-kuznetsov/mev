package edu.dfci.cccb.mev.web.test.wgcna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dfci.cccb.mev.dataset.domain.contract.*;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.DispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.PersistenceConfiguration;
import edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations;
import edu.dfci.cccb.mev.wgcna.domain.Wgcna;
import edu.dfci.cccb.mev.wgcna.domain.WgcnaBuilder;
import edu.dfci.cccb.mev.wgcna.rest.configuration.WgcnaConfiguration;
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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by antony on 5/27/16.
 */
@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        DispatcherConfiguration.class,
        PersistenceConfiguration.class,
        ContainerConfigurations.class,
        DatasetRestConfiguration.class,
        RDispatcherConfiguration.class,
        WgcnaConfiguration.class
})
public class TestWgcnaController {
    @Autowired WebApplicationContext applicationContext;
    private MockMvc mockMvc;
    private MockHttpSession mockHttpSession;
    @Inject DatasetBuilder datasetBuilder;
    private @Inject Workspace workspace;
    private @Inject ObjectMapper jsonObjectMapper;
    Dataset dataset;
    @Inject @Named("mev.analysis.wgcna.builder") Provider<WgcnaBuilder> builderProvider;

    @Before
    public void setup() throws DatasetBuilderException, InvalidDatasetNameException, InvalidDimensionTypeException, IOException {
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

    @Test @Ignore
    public void testAsync () throws Exception {

        String analysisName = "wgcna_test";
        Wgcna.Parameters params = new Wgcna.Parameters(analysisName, null, null, "euclidean", 0.5, 1000, true);
        log.debug("WgcnaParameters" + jsonObjectMapper.writeValueAsString(params));
        @SuppressWarnings ("unused")
        MvcResult mvcResultPUT = this.mockMvc.perform(
                put(String.format("/dataset/%s/analyze/wgcna/%s", dataset.name(), analysisName))
                        .param ("format", "json")
                        .contentType (MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .session (mockHttpSession)
                        .content(jsonObjectMapper.writeValueAsString(params))
        )
                .andExpect (status ().isOk ())
                .andDo(print())
                .andReturn ();

        //The first put will generate an AnalysisStatus object with "IN_PROGRESS" status
        Analysis analysisStatus = dataset.analyses ().get (analysisName);
        log.debug("******* AnalysisStatus:\n"+ jsonObjectMapper.writeValueAsString (analysisStatus));
        assertThat(analysisStatus.name (), is(analysisName));
        assertThat(analysisStatus.type (), is("wgcna"));
        assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));

        //Wait for analysis to complete
        Thread.sleep (2000L);

        //The second get should return the actual analysis
        Wgcna analysis = (Wgcna) dataset.analyses ().get (analysisName);
        log.debug("******* WgcnaAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));
        assertThat(analysis.name (), is(analysisName));
        assertThat (analysis.result(), not(nullValue ()));
        assertThat (analysis.status(), is(Analysis.MEV_ANALYSIS_STATUS_SUCCESS));
//        assertThat (analysis.sdev(), not(nullValue ()));


        MvcResult mvcResultGET = this.mockMvc.perform(
                get(String.format("/dataset/%s/analysis/%s", dataset.name(), analysisName))
                        .param ("format", "json")
                        .contentType (MediaType.APPLICATION_JSON)
                        .accept("application/json")
                        .session (mockHttpSession)
                        .param ("format", "json")
        )
                .andExpect (status ().isOk ())
                .andDo(print())
                .andReturn ();
        log.debug(String.format("mvcResultGET content: %s", mvcResultGET.getResponse ().getContentAsString ()));

        Wgcna analysisFromJson = jsonObjectMapper.readValue(mvcResultGET.getResponse ().getContentAsString (), Wgcna.class);
//    assertThat (analysisFromJson.result ().genes (), is(result.genes()));
//    assertThat (analysisFromJson.result ().mad(), is(result.mad()));
        assertThat (analysisFromJson.type(), is("wgcna"));
        assertThat (analysisFromJson.name(), is(analysisName));
    }
}
