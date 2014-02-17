package edu.dfci.cccb.mev.test.annotation.server.configuration;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsLoader;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;


@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ AnnotationServerConfigurationMock.class })
public class TestAnnotationServerConfiguration {

@Test
public void test(){
  
}

}
