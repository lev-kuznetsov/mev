package edu.dfci.cccb.mev.web.test.normalization.controller;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dfci.cccb.mev.dataset.domain.contract.*;
import edu.dfci.cccb.mev.dataset.domain.fs.FlatFileValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.normalization.domain.Normalization;
import edu.dfci.cccb.mev.normalization.rest.NormalizationConfiguration;
import edu.dfci.cccb.mev.normalization.rest.controllers.NormalizationController;
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

import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.DispatcherConfiguration;
import edu.dfci.cccb.mev.web.configuration.PersistenceConfiguration;
import edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes={DispatcherConfiguration.class, 
        PersistenceConfiguration.class, 
        ContainerConfigurations.class, 
        DatasetRestConfiguration.class,
        RDispatcherConfiguration.class,
		NormalizationConfiguration.class
        })
public class TestNormalizatinoController {

	@Autowired
	private WebApplicationContext applicationContext;
	private MockMvc mockMvc;
	private MockHttpSession mockHttpSession;
	@Inject DatasetBuilder datasetBuilder;
	private @Inject Workspace workspace;
	private @Inject
	ObjectMapper jsonObjectMapper;
	Dataset dataset;
	private @Inject Provider<SelectionBuilder> selectionBuilder;

	private Selection createSelection(String name, List<String> keys){
		//create column selections
		SelectionBuilder selectionBuilder = this.selectionBuilder.get();
		final Properties properties = new Properties ();
		properties.put ("selectionDescription", "first mock selection");
		properties.put ("selectionColor", "#ff0000");
		SimpleSelection selection = new SimpleSelection (name, properties, keys);
		return selection;
	}

	@Before
	public void setup() throws IOException, InvalidDimensionTypeException, InvalidDatasetNameException, DatasetBuilderException {
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

		//put column selection
		dataset.dimension (Dimension.Type.COLUMN).selections ().put (
				createSelection("s1", Arrays.asList("A", "B"))
		);
		//put row selection
		dataset.dimension (Dimension.Type.ROW).selections ().put (
				createSelection("g1", Arrays.asList("Sell", "Cars"))
		);

		workspace.put (dataset);
	}
	@Test @Ignore
	public void testAsync() throws Exception {
		Normalization.NormalizationParameters dto = new Normalization.NormalizationParameters("css", "normcss", dataset.name()+"--normcss");
		MvcResult mvcResult = this.mockMvc.perform(
				put(String.format("/dataset/%s/analyze/normalization/%s", dataset.name(), dto.name()))
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
		assertThat(analysisStatus.type (), is("normalization"));
		assertThat(analysisStatus.status (), is(Analysis.MEV_ANALYSIS_STATUS_IN_PROGRESS));

		//Wait for analysis to complete
		Thread.sleep (15000L);

		//get the analysis directly from workspace
		log.debug("**************************");
		Normalization analysis =  (Normalization)dataset.analyses ().get (dto.name());
		log.debug("******* NormalizationAnalysis:\n"+ jsonObjectMapper.writeValueAsString (analysis));
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
	    Normalization analysisFromGET = jsonObjectMapper.readValue (mvcResultGET.getResponse ().getContentAsString (), Normalization.class);
		assertThat (analysisFromGET.name (), is(dto.name()));
		assertThat(analysisFromGET.status (), is(Analysis.MEV_ANALYSIS_STATUS_SUCCESS));
	}

	@Test @Ignore
	public void testExport() throws Exception{
		//first run the normalization analysis
		testAsync();
		Normalization analysis = (Normalization) dataset.analyses().get("normcss");
		NormalizationController.NormalizationExportParams dto = new NormalizationController.NormalizationExportParams(analysis.params().name());
		MvcResult mvcResult = this.mockMvc.perform(
				put(String.format("/dataset/%s/analyze/normalization/%s/export", dataset.name(), dto.name()))
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
		Dataset normalized = workspace.get (analysis.params().exportName());
		assertThat(normalized.name (), is(analysis.params().exportName()));
		assertThat(normalized.dimension(Type.COLUMN).selections().get("s1").keys(), is(Arrays.asList("A", "B")));
		assertThat(normalized.dimension(Type.ROW).selections().get("g1").keys(), is(Arrays.asList("Sell", "Cars")));
		log.debug("******* NormalizationExport:\n"+ jsonObjectMapper.writeValueAsString (normalized));
	}


}
