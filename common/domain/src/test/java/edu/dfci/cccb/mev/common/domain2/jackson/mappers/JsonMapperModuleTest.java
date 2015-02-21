package edu.dfci.cccb.mev.common.domain2.jackson.mappers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provides;

import edu.dfci.cccb.mev.common.domain2.annotation.Json;
import edu.dfci.cccb.mev.common.domain2.jackson.guice.JsonMapperModule;

@RunWith (JukitoRunner.class)
public class JsonMapperModuleTest {

  public static class JsonTestModule extends JukitoModule {
    @Provides
    @Named ("hello")
    public String hello () {
      return "world";
    }

    protected void configureTest () {
      install (new JsonMapperModule ());
    }
  }

  public static class JackonPojo {
    private @JsonProperty String hello = "hello";
  }

  @XmlRootElement
  @XmlAccessorType (XmlAccessType.NONE)
  public static class JaxbPojo {
    private @XmlElement String hello = "hello";
  }

  @XmlRootElement
  @XmlAccessorType (XmlAccessType.NONE)
  public static class InjectedPojo {
    private @Inject @Named ("hello") String hello;
  }

  @Test
  public void writeJaxb (@Json ObjectMapper m) throws Exception {
    assertThat (m.writeValueAsString (new JaxbPojo ()), is ("{\"hello\":\"hello\"}"));
  }

  @Test
  public void parseJaxb (@Json ObjectMapper m) throws Exception {
    assertThat (m.readValue ("{\"hello\":\"world\"}", JaxbPojo.class).hello, is ("world"));
  }

  @Test
  public void writeJackson (@Json ObjectMapper m) throws Exception {
    assertThat (m.writeValueAsString (new JackonPojo ()), is ("{\"hello\":\"hello\"}"));
  }

  @Test
  public void parseJackson (@Json ObjectMapper m) throws Exception {
    assertThat (m.readValue ("{\"hello\":\"world\"}", JackonPojo.class).hello, is ("world"));
  }

  @Test
  public void parseInjected (@Json ObjectMapper m) throws Exception {
    assertThat (m.readValue ("{}", InjectedPojo.class).hello, is ("world"));
  }
}
